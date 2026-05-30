package com.carfind.service;

import com.carfind.dto.QuizResponse;
import com.carfind.dto.RecommendationResult;
import com.carfind.model.Car;
import com.carfind.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private CarRepository carRepository;

    public List<RecommendationResult> getRecommendations(QuizResponse quiz) {
        List<Car> allCars = carRepository.findAll();
        List<RecommendationResult> results = new ArrayList<>();

        for (Car car : allCars) {
            double score = calculateMatchScore(car, quiz);
            String suitcaseCapacity = translateBootSpace(car.getBootSpace());
            results.add(new RecommendationResult(car, (int) Math.round(score), suitcaseCapacity));
        }

        // Sort by match score in descending order
        return results.stream()
                .sorted((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()))
                .collect(Collectors.toList());
    }

    private double calculateMatchScore(Car car, QuizResponse quiz) {
        // 1. Determine weights based on priorities
        double wBudget = 0.30;
        double wSafety = 0.20;
        double wMileage = 0.20;
        double wFuel = 0.15;
        double wFeatures = 0.15;

        // If User priority contains 'Safety', double safety rating weight (20% -> 40%)
        // and adjust the other weights proportionally to sum up to 100%
        if (quiz.getPriorities() != null && quiz.getPriorities().stream().anyMatch(p -> p.equalsIgnoreCase("Safety"))) {
            wSafety = 0.40;
            wBudget = 0.225;
            wMileage = 0.15;
            wFuel = 0.1125;
            wFeatures = 0.1125;
        }

        // 2. Budget Score (0 - 100)
        double budgetScore = 0.0;
        if (car.getPriceMin() <= quiz.getMaxBudget() && car.getPriceMax() >= quiz.getMinBudget()) {
            budgetScore = 100.0; // Perfect budget fit
        } else if (car.getPriceMin() > quiz.getMaxBudget()) {
            // Over budget penalty: deduct 15 points per Lakh INR over the limit
            double overage = car.getPriceMin() - quiz.getMaxBudget();
            budgetScore = Math.max(0.0, 100.0 - (overage * 15.0));
        } else if (car.getPriceMax() < quiz.getMinBudget()) {
            // Under budget: cheap is fine, but maybe not what they targeted. Slight penalty, capped at 50 min.
            double underage = quiz.getMinBudget() - car.getPriceMax();
            budgetScore = Math.max(50.0, 100.0 - (underage * 4.0));
        }

        // 3. Safety Score (0 - 100)
        double safetyScore = car.getSafetyRating() * 20.0; // 5 stars = 100 points

        // 4. Mileage Score (0 - 100)
        double mileageScore = 0.0;
        if ("EV".equalsIgnoreCase(car.getFuelType())) {
            mileageScore = 100.0; // EVs get maximum efficiency score
        } else {
            // standard ICE/CNG mileage ranges from 10 to 25 km/l
            double mileage = car.getMileage();
            if (mileage >= 25.0) {
                mileageScore = 100.0;
            } else if (mileage <= 10.0) {
                mileageScore = 0.0;
            } else {
                mileageScore = ((mileage - 10.0) / 15.0) * 100.0;
            }
        }

        // 5. Fuel Type Preference Score (0 or 100)
        double fuelScore = 100.0;
        if (quiz.getPreferredFuel() != null && !quiz.getPreferredFuel().equalsIgnoreCase("No Preference") && !quiz.getPreferredFuel().isEmpty()) {
            if (!car.getFuelType().equalsIgnoreCase(quiz.getPreferredFuel())) {
                fuelScore = 0.0; // Does not match user's explicit preference
            }
        }

        // 6. Features Score (0 - 100)
        double featuresScore = 100.0;
        if (quiz.getFeatures() != null && !quiz.getFeatures().isEmpty()) {
            double requestedCount = quiz.getFeatures().size();
            double matchedCount = 0.0;

            for (String feature : quiz.getFeatures()) {
                if (feature.equalsIgnoreCase("sunroof") && car.isSunroof()) matchedCount++;
                else if (feature.equalsIgnoreCase("adas") && car.isAdas()) matchedCount++;
                else if (feature.equalsIgnoreCase("cam360") && car.isCam360()) matchedCount++;
                else if (feature.equalsIgnoreCase("ventilatedSeats") && car.isVentilatedSeats()) matchedCount++;
            }
            featuresScore = (matchedCount / requestedCount) * 100.0;
        }

        // Calculate weighted score
        double finalScore = (budgetScore * wBudget) +
                             (safetyScore * wSafety) +
                             (mileageScore * wMileage) +
                             (fuelScore * wFuel) +
                             (featuresScore * wFeatures);

        // 7. Non-trivial City Use Penalty
        // "If User usage is 'City', penalize low-mileage cars."
        if (quiz.getUseCase() != null && quiz.getUseCase().equalsIgnoreCase("City")) {
            // ICE Low Mileage Penalty (< 14.5 km/l for Petrol/Diesel, < 17 km/kg for CNG. EVs exempt)
            if (!"EV".equalsIgnoreCase(car.getFuelType())) {
                if ("CNG".equalsIgnoreCase(car.getFuelType()) && car.getMileage() < 18.0) {
                    finalScore -= 15.0;
                } else if (("Petrol".equalsIgnoreCase(car.getFuelType()) || "Diesel".equalsIgnoreCase(car.getFuelType())) && car.getMileage() < 14.5) {
                    finalScore -= 15.0;
                }
            }
        }

        // 8. Family Size segment tuning for enhanced accuracy
        if (quiz.getFamilySize() != null) {
            String family = quiz.getFamilySize();
            String segment = car.getSegment();
            if (family.equalsIgnoreCase("7+")) {
                // Must be a large car (MUV or large SUV). Deduct 25 points if it's Hatchback or Sedan.
                if (segment.equalsIgnoreCase("Hatchback") || segment.equalsIgnoreCase("Sedan")) {
                    finalScore -= 25.0;
                }
            } else if (family.equalsIgnoreCase("5")) {
                // Prefer Sedans, SUVs, and MUVs. Hatchbacks are slightly small.
                if (segment.equalsIgnoreCase("Hatchback")) {
                    finalScore -= 8.0;
                }
            } else if (family.equalsIgnoreCase("2-4")) {
                // Hatchback, Sedan, SUV are great. Large MUVs are too bulky.
                if (segment.equalsIgnoreCase("MUV")) {
                    finalScore -= 8.0;
                }
            }
        }

        // Keep final score bounded between 0 and 100
        return Math.max(0.0, Math.min(100.0, finalScore));
    }

    public String translateBootSpace(int bootSpace) {
        if (bootSpace >= 500) {
            return "4 Large Suitcases + 2 Small Bags";
        } else if (bootSpace >= 400) {
            return "3 Large Suitcases + 1 Small Bag";
        } else if (bootSpace >= 300) {
            return "2 Large Suitcases + 2 Small Carry-ons";
        } else if (bootSpace >= 200) {
            return "1 Large Suitcase + 2 Small Carry-ons";
        } else {
            return "2 Small Carry-ons (Compact)";
        }
    }
}

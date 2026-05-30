package com.carfind.controller;

import com.carfind.dto.QuizResponse;
import com.carfind.dto.RecommendationResult;
import com.carfind.model.Car;
import com.carfind.repository.CarRepository;
import com.carfind.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Car Decision Engine", description = "Endpoints for car recommendations and catalogue browsing")
public class MatchController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private CarRepository carRepository;

    @Operation(
        summary = "Get ranked car recommendations",
        description = "Submit a quiz payload describing budget range, use-case, family size, " +
                      "fuel preference, priorities and desired features. Returns all 103 cars " +
                      "sorted by their computed match score (0–100). " +
                      "Safety priority doubles the safety weight; City use-case penalises low-mileage cars."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ranked recommendation list",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecommendationResult.class)))),
        @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    })
    @PostMapping("/recommend")
    public List<RecommendationResult> getRecommendations(@RequestBody QuizResponse quiz) {
        return recommendationService.getRecommendations(quiz);
    }

    @Operation(
        summary = "List all cars in the catalogue",
        description = "Returns all 103 seeded Indian car models from the H2 in-memory database. " +
                      "Covers Maruti, Tata, Hyundai, Mahindra, Kia, Toyota, Honda, MG, Skoda, " +
                      "Volkswagen, Jeep, Citroen, BYD across Petrol, Diesel, CNG and EV fuel types."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Full car catalogue",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Car.class))))
    })
    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}

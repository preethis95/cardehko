package com.carfind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Quiz payload submitted by the user to drive the recommendation engine")
public class QuizResponse {

    @Schema(description = "Minimum budget in Lakhs INR", example = "8", minimum = "3")
    private double minBudget;

    @Schema(description = "Maximum budget in Lakhs INR", example = "22", maximum = "80")
    private double maxBudget;

    @Schema(description = "Primary driving use-case", example = "City", allowableValues = {"City", "Highway", "Mixed"})
    private String useCase;

    @Schema(description = "Number of regular occupants", example = "2-4", allowableValues = {"2-4", "5", "7+"})
    private String familySize;

    @Schema(description = "Preferred fuel type; 'No Preference' disables the fuel filter",
            example = "Petrol",
            allowableValues = {"Petrol", "Diesel", "CNG", "EV", "No Preference"})
    private String preferredFuel;

    @Schema(description = "Driving priorities (multi-select). 'Safety' doubles the safety scoring weight.",
            example = "[\"Safety\", \"Mileage\"]",
            allowableValues = {"Safety", "Mileage", "Comfort", "Performance"})
    private List<String> priorities;

    @Schema(description = "Must-have features (multi-select). Cars missing requested features lose score proportionally.",
            example = "[\"sunroof\", \"adas\"]",
            allowableValues = {"sunroof", "adas", "cam360", "ventilatedSeats"})
    private List<String> features;
}

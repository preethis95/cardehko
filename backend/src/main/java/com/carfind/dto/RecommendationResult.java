package com.carfind.dto;

import com.carfind.model.Car;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A single scored recommendation returned by the decision engine")
public class RecommendationResult {

    @Schema(description = "The full car object from the catalogue")
    private Car car;

    @Schema(description = "Computed match score from 0 to 100 based on the quiz inputs", example = "92", minimum = "0", maximum = "100")
    private int matchScore;

    @Schema(description = "Human-readable boot space translation", example = "2 Large Suitcases + 2 Small Carry-ons")
    private String suitcaseCapacity;
}

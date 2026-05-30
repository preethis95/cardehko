package com.carfind.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "An Indian car model in the CarFind catalogue")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated database ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Model name", example = "Nexon")
    private String name;

    @Schema(description = "Manufacturer brand", example = "Tata")
    private String brand;

    @Schema(description = "Minimum ex-showroom price in Lakhs INR", example = "8.1")
    private double priceMin;

    @Schema(description = "Maximum ex-showroom price in Lakhs INR", example = "15.5")
    private double priceMax;

    @Schema(description = "NCAP / GNCAP safety rating (0–5 stars)", example = "5", minimum = "0", maximum = "5")
    private int safetyRating;

    @Schema(description = "Fuel efficiency — km/l for ICE, km/kg for CNG, km range for EV", example = "17.4")
    private double mileage;

    @Schema(description = "Fuel type", example = "Petrol", allowableValues = {"Petrol", "Diesel", "CNG", "EV"})
    private String fuelType;

    @Schema(description = "Body segment", example = "SUV", allowableValues = {"Hatchback", "Sedan", "SUV", "MUV"})
    private String segment;

    @Schema(description = "Boot / cargo space in litres", example = "382")
    private int bootSpace;

    @Schema(description = "Has panoramic sunroof", example = "true")
    private boolean sunroof;

    @Schema(description = "Has ADAS (Advanced Driver Assistance System) suite", example = "false")
    private boolean adas;

    @Schema(description = "Has 360° surround-view camera", example = "false")
    private boolean cam360;

    @Schema(description = "Has ventilated / cooled front seats", example = "false")
    private boolean ventilatedSeats;
}

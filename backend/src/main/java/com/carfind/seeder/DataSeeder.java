package com.carfind.seeder;

import com.carfind.model.Car;
import com.carfind.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CarRepository carRepository;

    @Override
    public void run(String... args) throws Exception {
        if (carRepository.count() == 0) {
            seedCars();
        }
    }

    private void seedCars() {
        // --- MARUTI SUZUKI (15 Cars) ---
        add("Alto K10", "Maruti Suzuki", 3.99, 5.96, 2, 24.39, "Petrol", "Hatchback", 214, false, false, false, false);
        add("S-Presso", "Maruti Suzuki", 4.26, 6.12, 0, 24.76, "Petrol", "Hatchback", 270, false, false, false, false);
        add("Celerio", "Maruti Suzuki", 5.37, 7.09, 0, 25.24, "Petrol", "Hatchback", 313, false, false, false, false);
        add("Wagon R", "Maruti Suzuki", 5.54, 7.42, 1, 24.35, "Petrol", "Hatchback", 341, false, false, false, false);
        add("Wagon R CNG", "Maruti Suzuki", 6.45, 6.89, 1, 34.05, "CNG", "Hatchback", 230, false, false, false, false);
        add("Swift", "Maruti Suzuki", 6.49, 9.64, 3, 24.80, "Petrol", "Hatchback", 265, false, false, false, false);
        add("Dzire", "Maruti Suzuki", 6.57, 9.39, 2, 22.41, "Petrol", "Sedan", 378, false, false, false, false);
        add("Baleno", "Maruti Suzuki", 6.66, 9.88, 0, 22.35, "Petrol", "Hatchback", 318, false, false, true, false);
        add("Baleno CNG", "Maruti Suzuki", 8.35, 9.28, 0, 30.61, "CNG", "Hatchback", 220, false, false, false, false);
        add("Fronx", "Maruti Suzuki", 7.51, 13.04, 3, 21.79, "Petrol", "SUV", 308, false, false, true, false);
        add("Brezza", "Maruti Suzuki", 8.34, 14.14, 4, 17.38, "Petrol", "SUV", 328, true, false, true, false);
        add("Ertiga", "Maruti Suzuki", 8.69, 13.03, 3, 20.51, "Petrol", "MUV", 550, false, false, false, false);
        add("XL6", "Maruti Suzuki", 11.61, 14.77, 3, 20.97, "Petrol", "MUV", 209, false, false, true, true);
        add("Grand Vitara", "Maruti Suzuki", 10.80, 20.09, 4, 27.97, "Petrol", "SUV", 373, true, false, true, true);
        add("Jimny", "Maruti Suzuki", 12.74, 14.79, 3, 16.94, "Petrol", "SUV", 208, false, false, false, false);

        // --- TATA (14 Cars) ---
        add("Tiago", "Tata", 5.60, 8.20, 4, 19.01, "Petrol", "Hatchback", 242, false, false, false, false);
        add("Tiago EV", "Tata", 7.99, 11.89, 4, 250.0, "EV", "Hatchback", 240, false, false, false, false);
        add("Tigor", "Tata", 6.30, 8.95, 4, 19.28, "Petrol", "Sedan", 419, false, false, false, false);
        add("Altroz", "Tata", 6.60, 10.74, 5, 19.33, "Petrol", "Hatchback", 345, true, false, false, false);
        add("Punch", "Tata", 6.13, 10.20, 5, 20.09, "Petrol", "SUV", 366, true, false, false, false);
        add("Punch EV", "Tata", 10.99, 15.49, 5, 315.0, "EV", "SUV", 366, true, false, true, true);
        add("Nexon", "Tata", 8.00, 15.80, 5, 17.44, "Petrol", "SUV", 382, true, true, true, true);
        add("Nexon EV", "Tata", 14.49, 19.29, 5, 465.0, "EV", "SUV", 350, true, false, true, true);
        add("Nexon Diesel", "Tata", 11.10, 15.60, 5, 23.23, "Diesel", "SUV", 382, true, false, true, true);
        add("Harrier", "Tata", 15.49, 26.44, 5, 16.80, "Diesel", "SUV", 445, true, true, true, true);
        add("Safari", "Tata", 16.19, 27.34, 5, 16.30, "Diesel", "SUV", 420, true, true, true, true);
        add("Curvv", "Tata", 10.00, 19.00, 5, 17.50, "Petrol", "SUV", 422, true, true, true, true);
        add("Curvv EV", "Tata", 17.49, 21.99, 5, 502.0, "EV", "SUV", 500, true, true, true, true);
        add("Tiago CNG", "Tata", 6.55, 8.35, 4, 26.49, "CNG", "Hatchback", 210, false, false, false, false);

        // --- HYUNDAI (13 Cars) ---
        add("Grand i10 Nios", "Hyundai", 5.92, 8.56, 2, 20.70, "Petrol", "Hatchback", 260, false, false, false, false);
        add("Aura", "Hyundai", 6.49, 9.05, 2, 20.50, "Petrol", "Sedan", 402, false, false, false, false);
        add("i20", "Hyundai", 7.04, 11.21, 3, 20.35, "Petrol", "Hatchback", 311, true, false, false, false);
        add("i20 N Line", "Hyundai", 9.99, 12.52, 3, 20.00, "Petrol", "Hatchback", 311, true, false, false, false);
        add("Exter", "Hyundai", 6.13, 10.28, 3, 19.40, "Petrol", "SUV", 391, true, false, false, false);
        add("Exter CNG", "Hyundai", 8.43, 9.31, 3, 27.10, "CNG", "SUV", 290, false, false, false, false);
        add("Venue", "Hyundai", 7.94, 13.48, 3, 17.50, "Petrol", "SUV", 350, true, true, false, false);
        add("Creta", "Hyundai", 11.00, 20.15, 3, 17.40, "Petrol", "SUV", 433, true, true, true, true);
        add("Creta Diesel", "Hyundai", 12.56, 20.15, 3, 21.80, "Diesel", "SUV", 433, true, true, true, true);
        add("Alcazar", "Hyundai", 16.77, 21.28, 3, 18.10, "Petrol", "SUV", 579, true, true, true, true);
        add("Verna", "Hyundai", 11.00, 17.42, 5, 20.60, "Petrol", "Sedan", 528, true, true, false, true);
        add("Tucson", "Hyundai", 29.02, 35.94, 5, 18.00, "Diesel", "SUV", 540, true, true, true, true);
        add("Ioniq 5", "Hyundai", 46.05, 46.05, 5, 631.0, "EV", "SUV", 571, false, true, true, true);

        // --- MAHINDRA (11 Cars) ---
        add("XUV3XO", "Mahindra", 7.49, 15.49, 5, 18.89, "Petrol", "SUV", 364, true, true, true, false);
        add("Thar", "Mahindra", 11.35, 17.60, 4, 15.20, "Diesel", "SUV", 150, false, false, false, false);
        add("Thar Roxx", "Mahindra", 12.99, 22.49, 5, 12.40, "Diesel", "SUV", 447, true, true, true, true);
        add("XUV700", "Mahindra", 13.99, 26.99, 5, 13.00, "Petrol", "SUV", 450, true, true, true, true);
        add("XUV700 Diesel", "Mahindra", 14.49, 26.99, 5, 16.00, "Diesel", "SUV", 450, true, true, true, true);
        add("Scorpio-N", "Mahindra", 13.85, 24.54, 5, 12.75, "Petrol", "SUV", 460, true, false, false, false);
        add("Scorpio-N Diesel", "Mahindra", 14.25, 24.54, 5, 15.20, "Diesel", "SUV", 460, true, true, true, true);
        add("Scorpio Classic", "Mahindra", 13.62, 17.42, 0, 14.00, "Diesel", "SUV", 460, false, false, false, false);
        add("Bolero", "Mahindra", 9.90, 10.91, 1, 16.00, "Diesel", "SUV", 550, false, false, false, false);
        add("Bolero Neo", "Mahindra", 9.90, 12.15, 1, 17.29, "Diesel", "SUV", 384, false, false, false, false);
        add("XUV400 EV", "Mahindra", 15.49, 17.49, 5, 456.0, "EV", "SUV", 378, true, false, false, false);

        // --- KIA (8 Cars) ---
        add("Sonet", "Kia", 7.99, 15.75, 3, 18.70, "Petrol", "SUV", 385, true, true, true, true);
        add("Sonet Diesel", "Kia", 9.80, 15.75, 3, 22.30, "Diesel", "SUV", 385, true, true, true, true);
        add("Seltos", "Kia", 10.90, 20.35, 3, 17.70, "Petrol", "SUV", 433, true, true, true, true);
        add("Seltos Diesel", "Kia", 12.00, 20.35, 3, 20.70, "Diesel", "SUV", 433, true, true, true, true);
        add("Carens", "Kia", 10.52, 19.67, 3, 17.90, "Petrol", "MUV", 216, true, false, false, true);
        add("Carens Diesel", "Kia", 12.70, 19.67, 3, 21.30, "Diesel", "MUV", 216, true, true, true, true);
        add("Carnival", "Kia", 63.90, 63.90, 5, 14.85, "Diesel", "MUV", 540, true, true, true, true);
        add("EV6", "Kia", 60.95, 65.95, 5, 528.0, "EV", "SUV", 520, true, true, true, true);

        // --- TOYOTA (9 Cars) ---
        add("Glanza", "Toyota", 6.86, 10.00, 0, 22.35, "Petrol", "Hatchback", 318, false, false, true, false);
        add("Rumion", "Toyota", 10.44, 13.73, 3, 20.51, "Petrol", "MUV", 550, false, false, false, false);
        add("Taisor", "Toyota", 7.74, 13.08, 3, 21.70, "Petrol", "SUV", 308, false, false, true, false);
        add("Hyryder", "Toyota", 11.14, 20.19, 4, 27.97, "Petrol", "SUV", 373, true, false, true, true);
        add("Innova Crysta", "Toyota", 19.99, 26.30, 5, 12.00, "Diesel", "MUV", 300, false, false, false, false);
        add("Innova Hycross", "Toyota", 19.77, 30.98, 5, 23.24, "Petrol", "MUV", 300, true, true, true, true);
        add("Fortuner", "Toyota", 33.43, 51.44, 5, 10.00, "Diesel", "SUV", 296, false, false, false, true);
        add("Legender", "Toyota", 43.66, 47.64, 5, 10.50, "Diesel", "SUV", 296, false, false, true, true);
        add("Camry Hybrid", "Toyota", 46.17, 46.17, 5, 22.70, "Petrol", "Sedan", 524, true, true, false, true);

        // --- HONDA (5 Cars) ---
        add("Amaze", "Honda", 7.20, 9.96, 4, 18.60, "Petrol", "Sedan", 420, false, false, false, false);
        add("City", "Honda", 11.82, 16.35, 5, 17.80, "Petrol", "Sedan", 506, true, true, false, false);
        add("City Hybrid", "Honda", 19.00, 20.55, 5, 27.13, "Petrol", "Sedan", 410, true, true, true, false);
        add("Elevate", "Honda", 11.69, 16.51, 5, 15.31, "Petrol", "SUV", 458, true, true, false, false);
        add("Amaze CNG", "Honda", 8.40, 9.20, 4, 25.00, "CNG", "Sedan", 320, false, false, false, false);

        // --- MG (7 Cars) ---
        add("Comet EV", "MG", 6.99, 9.53, 3, 230.0, "EV", "Hatchback", 150, false, false, false, false);
        add("Windsor EV", "MG", 13.50, 15.50, 5, 331.0, "EV", "SUV", 604, true, false, true, true);
        add("ZS EV", "MG", 18.98, 25.44, 5, 461.0, "EV", "SUV", 470, true, true, true, true);
        add("Astor", "MG", 9.98, 18.08, 5, 15.43, "Petrol", "SUV", 400, true, true, true, false);
        add("Hector", "MG", 13.99, 22.24, 4, 13.79, "Petrol", "SUV", 587, true, true, true, true);
        add("Hector Diesel", "MG", 17.70, 22.24, 4, 15.58, "Diesel", "SUV", 587, true, true, true, true);
        add("Gloster", "MG", 38.80, 43.87, 5, 12.04, "Diesel", "SUV", 343, true, true, true, true);

        // --- RENAULT (4 Cars) ---
        add("Kwid", "Renault", 4.70, 6.45, 1, 21.46, "Petrol", "Hatchback", 279, false, false, false, false);
        add("Triber", "Renault", 6.00, 8.98, 4, 18.20, "Petrol", "MUV", 625, false, false, false, false);
        add("Kiger", "Renault", 6.00, 11.23, 4, 20.50, "Petrol", "SUV", 405, false, false, true, false);
        add("Kiger Turbo", "Renault", 9.30, 11.23, 4, 19.70, "Petrol", "SUV", 405, false, false, true, false);

        // --- NISSAN (3 Cars) ---
        add("Magnite", "Nissan", 6.00, 11.11, 4, 19.38, "Petrol", "SUV", 336, false, false, true, false);
        add("Magnite Turbo", "Nissan", 8.25, 11.27, 4, 20.00, "Petrol", "SUV", 336, false, false, true, false);
        add("Magnite AMT", "Nissan", 6.60, 8.90, 4, 19.70, "Petrol", "SUV", 336, false, false, false, false);

        // --- SKODA (5 Cars) ---
        add("Slavia 1.0", "Skoda", 11.39, 17.43, 5, 20.32, "Petrol", "Sedan", 521, true, false, false, false);
        add("Slavia 1.5", "Skoda", 15.00, 18.69, 5, 18.73, "Petrol", "Sedan", 521, true, false, true, true);
        add("Kushaq 1.0", "Skoda", 10.89, 17.89, 5, 19.76, "Petrol", "SUV", 385, true, false, false, false);
        add("Kushaq 1.5", "Skoda", 15.99, 19.79, 5, 18.60, "Petrol", "SUV", 385, true, false, true, true);
        add("Kodiaq", "Skoda", 39.99, 39.99, 5, 12.78, "Petrol", "SUV", 270, true, false, true, true);

        // --- VOLKSWAGEN (5 Cars) ---
        add("Virtus 1.0", "Volkswagen", 11.56, 17.60, 5, 20.08, "Petrol", "Sedan", 521, true, false, false, false);
        add("Virtus 1.5", "Volkswagen", 16.80, 19.15, 5, 18.67, "Petrol", "Sedan", 521, true, false, true, true);
        add("Taigun 1.0", "Volkswagen", 11.70, 18.00, 5, 19.20, "Petrol", "SUV", 385, true, false, false, false);
        add("Taigun 1.5", "Volkswagen", 16.90, 19.70, 5, 18.10, "Petrol", "SUV", 385, true, false, true, true);
        add("Tiguan", "Volkswagen", 35.17, 35.17, 5, 12.65, "Petrol", "SUV", 615, true, false, false, false);

        // --- CITROEN & BYD (4 Cars) ---
        add("C3", "Citroen", 6.16, 9.12, 0, 19.30, "Petrol", "Hatchback", 315, false, false, false, false);
        add("eC3", "Citroen", 11.61, 13.41, 0, 320.0, "EV", "Hatchback", 315, false, false, false, false);
        add("Atto 3", "BYD", 24.99, 33.99, 5, 521.0, "EV", "SUV", 440, true, true, true, true);
        add("BYD Seal", "BYD", 41.00, 53.00, 5, 650.0, "EV", "Sedan", 400, true, true, true, true);
    }

    private void add(String name, String brand, double priceMin, double priceMax, int safetyRating, double mileage,
                     String fuelType, String segment, int bootSpace, boolean sunroof, boolean adas, boolean cam360, boolean ventilatedSeats) {
        Car car = new Car();
        car.setName(name);
        car.setBrand(brand);
        car.setPriceMin(priceMin);
        car.setPriceMax(priceMax);
        car.setSafetyRating(safetyRating);
        car.setMileage(mileage);
        car.setFuelType(fuelType);
        car.setSegment(segment);
        car.setBootSpace(bootSpace);
        car.setSunroof(sunroof);
        car.setAdas(adas);
        car.setCam360(cam360);
        car.setVentilatedSeats(ventilatedSeats);
        carRepository.save(car);
    }
}

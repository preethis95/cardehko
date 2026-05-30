# 🚗 CarFind — Indian Car Decision Engine

> **Find your perfect Indian car match in under 2 minutes.**  
> A full-stack recommendation engine powered by Spring Boot and React TypeScript, featuring a mathematical scoring algorithm across 103 real Indian car models.

---

## 📸 Application Overview

CarFind guides users through a 6-step quiz — budget range, driving use-case, family size, fuel preference, priorities, and desired features — then scores every car in the database against those inputs and returns a ranked list with boot-space translations.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Frontend** | React 18, TypeScript, Vite, SCSS Modules |
| **Backend** | Spring Boot 2.7.18 (Java 8), Maven |
| **Database** | H2 (in-memory, embedded) |
| **ORM** | Spring Data JPA / Hibernate |
| **API Docs** | SpringDoc OpenAPI (Swagger UI) |
| **Fonts** | Inter, Outfit (Google Fonts) |

---

## 📁 Project Structure

```
cardehko/
├── backend/                          # Spring Boot REST API
│   └── src/main/java/com/carfind/
│       ├── CarFindApplication.java   # Entry point
│       ├── config/
│       │   ├── CorsConfig.java       # Global CORS policy
│       │   └── OpenApiConfig.java    # Swagger / OpenAPI metadata
│       ├── controller/
│       │   └── MatchController.java  # POST /api/recommend, GET /api/cars
│       ├── dto/
│       │   ├── QuizResponse.java     # Incoming quiz payload
│       │   └── RecommendationResult.java # Outgoing result object
│       ├── model/
│       │   └── Car.java              # JPA entity
│       ├── repository/
│       │   └── CarRepository.java    # JPA repository
│       ├── seeder/
│       │   └── DataSeeder.java       # Seeds 103 Indian cars on startup
│       └── service/
│           └── RecommendationService.java # Core scoring algorithm
│
└── frontend/                         # React + TypeScript SPA
    └── src/
        ├── App.tsx                   # Root component, view-state router, theme
        ├── App.module.scss           # Layout and global component styles
        ├── index.css                 # Design system tokens (dark + light theme)
        └── components/
            ├── Quiz.tsx              # 6-step preference wizard
            ├── Quiz.module.scss
            ├── Results.tsx           # Ranked results grid + custom dropdown
            └── Results.module.scss
```

---

## 🧠 Scoring Algorithm

Every car receives a **match score from 0–100** composed of five weighted dimensions:

| Dimension | Default Weight | Notes |
|---|---|---|
| Budget Fit | 30% | Perfect fit = 100; over/under budget scaled with penalty |
| Safety Rating | 20% → **40%** | **Doubled** when user selects `Safety` as a priority |
| Mileage Efficiency | 20% | Scaled 10–25 km/l; EVs always score 100 |
| Fuel Preference | 15% | Binary 100/0 match; ignored on "No Preference" |
| Feature Match | 15% | Proportional: matched features ÷ requested features |

### Non-trivial Adjustments

- **City Penalty** — Cars with mileage below 14.5 km/l (petrol/diesel) or 18 km/kg (CNG) lose 15 points when use-case is `City`. EVs are exempt.
- **Family-size Segment Penalty** — 7+ family: hatchbacks/sedans lose 25 points. 5-person: hatchbacks lose 8 points. 2–4 person: MUVs lose 8 points.
- **Final score is clamped** to `[0, 100]`.

---

## 🚘 Car Database

103 real Indian cars seeded on every startup via `DataSeeder.java`:

| Brand | Models |
|---|---|
| Maruti Suzuki | 15 (Alto K10 → Grand Vitara, Invicto) |
| Tata | 12 (Tiago CNG → Curvv EV) |
| Hyundai | 13 (Grand i10 Nios → Ioniq 5) |
| Mahindra | 11 (XUV3XO → XUV400 EV) |
| Kia | 6 (Sonet → EV6) |
| Toyota | 6 (Glanza → Fortuner Diesel) |
| Honda | 5 (Amaze → Elevate) |
| MG | 4 (Hector → ZS EV) |
| Skoda | 4 (Slavia → Kodiaq) |
| Volkswagen | 3 (Taigun → Tiguan) |
| Others | Jeep Meridian, Citroen C3/eC3, BYD Atto 3, BYD Seal |

**Fuel Distribution:** Petrol 64 · Diesel 21 · EV 13 · CNG 5  
**Segment Distribution:** SUV 60 · Hatchback 20 · Sedan 14 · MUV 9

### Suitcase Capacity Translation

| Boot Space | Label |
|---|---|
| ≥ 500 L | 4 Large Suitcases + 2 Small Bags |
| ≥ 400 L | 3 Large Suitcases + 1 Small Bag |
| ≥ 300 L | 2 Large Suitcases + 2 Small Carry-ons |
| ≥ 200 L | 1 Large Suitcase + 2 Small Carry-ons |
| < 200 L | 2 Small Carry-ons (Compact) |

---

## 🚀 Getting Started

### Prerequisites

| Tool | Required Version |
|---|---|
| Java (JDK) | 1.8 (Java 8) — tested on Azul Zulu |
| Maven | Bundled via `./mvnw` wrapper |
| Node.js | v22.x (use NVM recommended) |
| npm | Bundled with Node |

### 1 · Start the Backend

```bash
cd backend

# Point JAVA_HOME to your Java 8 installation
export JAVA_HOME=/path/to/your/java8
export PATH=$JAVA_HOME/bin:$PATH

./mvnw clean spring-boot:run
```

The backend starts on **http://localhost:8080**.  
H2 seeds all 103 cars automatically.

### 2 · Start the Frontend

```bash
cd frontend

# If using NVM
nvm use 22

npm install
npm run dev
```

The frontend starts on **http://localhost:5173**.

---

## 🌐 API Reference

### Base URL
```
http://localhost:8080/api
```

### `POST /recommend`
Submit the quiz payload, receive a ranked list of all 103 cars.

**Request Body**
```json
{
  "minBudget": 8,
  "maxBudget": 22,
  "useCase": "City",
  "familySize": "2-4",
  "preferredFuel": "Petrol",
  "priorities": ["Safety", "Mileage"],
  "features": ["sunroof", "adas"]
}
```

| Field | Type | Allowed Values |
|---|---|---|
| `minBudget` | `double` | 3 – 80 (Lakhs INR) |
| `maxBudget` | `double` | 5 – 80 (Lakhs INR) |
| `useCase` | `string` | `City` · `Highway` · `Mixed` |
| `familySize` | `string` | `2-4` · `5` · `7+` |
| `preferredFuel` | `string` | `Petrol` · `Diesel` · `CNG` · `EV` · `No Preference` |
| `priorities` | `string[]` | `Safety` · `Mileage` · `Comfort` · `Performance` |
| `features` | `string[]` | `sunroof` · `adas` · `cam360` · `ventilatedSeats` |

**Response** — array sorted by `matchScore` descending:
```json
[
  {
    "car": {
      "id": 40,
      "name": "Verna",
      "brand": "Hyundai",
      "priceMin": 11.0,
      "priceMax": 17.42,
      "safetyRating": 5,
      "mileage": 20.6,
      "fuelType": "Petrol",
      "segment": "Sedan",
      "bootSpace": 528,
      "sunroof": true,
      "adas": true,
      "cam360": false,
      "ventilatedSeats": true
    },
    "matchScore": 96,
    "suitcaseCapacity": "4 Large Suitcases + 2 Small Bags"
  }
]
```

### `GET /cars`
Returns the full catalogue of 103 cars (unsorted).

---

## 📖 Developer Tools

| Tool | URL |
|---|---|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs |
| **H2 Database Console** | http://localhost:8080/h2-console |

**H2 Console credentials**
```
JDBC URL: jdbc:h2:mem:cardb
Username: sa
Password: (leave blank)
```

---

## 🎨 UI Features

- **Light / Dark theme** with `localStorage` persistence and smooth CSS variable transitions
- **6-step quiz wizard** with animated step transitions and a progress bar
- **Budget slider** with popular preset tiers (Budget Entry → High-End Elite)
- **Results grid** with SVG match-score radial rings, colour-coded green/amber/red
- **Custom sort dropdown** rendered via React Portal (no z-index / stacking context issues)
- **Quick filter badges** to narrow results by fuel type or body segment
- **"Top Match" ribbon** on the #1 scored card
- **Suitcase capacity insight** row on every card

---

## ⚠️ Known Limitations

| # | Area | Issue |
|---|---|---|
| 1 | **Data** | 8 cars have 0 safety rating (unrated models — S-Presso, Celerio, Baleno, eC3, etc.) |
| 2 | **Data** | CNG fleet is only 5 cars; users selecting CNG see limited results |
| 3 | **Scoring** | EV mileage (km range) is compared on the same 10–25 scale as ICE km/l — always scores 100 regardless of range quality differences |
| 4 | **Scoring** | `Comfort` and `Performance` priorities are collected in the quiz but have no additional weight in the scoring formula |
| 5 | **Budget** | Very tight budgets (₹3–5L) return all 103 cars (no hard exclusion) because the engine still assigns partial budget scores |
| 6 | **Persistence** | H2 in-memory — all data resets when the backend restarts |
| 7 | **Fuel filter** | Binary 0/100 scoring on fuel type; no partial credit for hybrid-compatible cars |

---

## 📄 License

MIT — free to use, modify, and distribute.

# iRonoc Portfolio Application - Architecture Overview

This document provides a comprehensive analysis of the system architecture, component breakdowns, and data flow pathways of the iRonoc portfolio application.

---

## 1. High-Level System Architecture

The iRonoc application is built as a modern, decoupled full-stack application. It leverages a high-performance **Java 25 / Spring Boot** backend and a responsive **React 19** single-page frontend.

```
                       +-----------------------------------+
                       |        Client Web Browser         |
                       +-----------------+-----------------+
                                         |
                                         | HTTP / HTTPS / WSS
                                         v
+----------------------------------------+-----------------------------------------+
|                                  Nginx / Gateway                                 |
+----------------------------------------+-----------------------------------------+
                                         |
                       +-----------------+-----------------+
                       |                                   | /api/* & /graphql
                       | Static Resources                  | (Reverse Proxy)
                       v                                   v
+----------------------------------------+ +---------------------------------------+
|            React Frontend              | |            Spring Boot Backend        |
|    (SPA: React Router 7, Material UI)  | |        (Embedded Tomcat container)    |
+----------------------------------------+ +-------------------+-------------------+
                                                               |
                                     +-------------------------+-------------------------+
                                     |                                                   |
                                     v (REST Endpoints)                                  v (GraphQL Endpoints)
                       +-------------+-------------+                       +-------------+-------------+
                       |    Spring Web Controllers |                       |   Spring GraphQL Controller |
                       | (Donate, Coffee, Activity)|                       |  (Brews, Donate, Portfolio) |
                       +-------------+-------------+                       +-------------+-------------+
                                     |                                                   |
                                     +-------------------------+-------------------------+
                                                               |
                                                               v
                                                   +-----------+-----------+
                                                   |      Service Layer    |
                                                   |  (GitDetails, Cache)  |
                                                   +-----------+-----------+
                                                               |
                                     +-------------------------+-------------------------+
                                     |                                                   |
                                     v                                                   v
                       +-------------+-------------+                       +-------------+-------------+
                       |    AWS Secrets Manager    |                       |      Third-Party APIs     |
                       |    (Secures API keys)     |                       |    (GitHub Repos/Issues)  |
                       +---------------------------+                       +---------------------------+
```

---

## 2. Core Technology Stack

### Frontend
- **Framework**: React 19 (ES6+)
- **Routing**: React Router 7 (Single-Page Routing)
- **Data Querying**: Apollo Client (GraphQL Client) & Native Fetch (REST)
- **UI Styling**: Material-UI (MUI 7) & Bootstrap 5
- **Visualizations**: Recharts (for charts and metrics)

### Backend
- **Core Platform**: Java 25 & Spring Boot 4.1
- **API Frameworks**: Spring Web (REST) & Spring GraphQL
- **Data Marshalling**: Jackson ObjectMapper
- **Security & Integrity**: Bucket4j (Rate Limiting Interceptors) & AWS SDK (Secrets Manager)
- **Routing Hooks**: PushStateResourceResolver (maps HTML5 history client-side routes back to React's `index.html`)

---

## 3. Frontend Architecture Overview

The frontend is implemented as a single-page React 19 application. It handles routing locally, renders structured layouts with components from MUI and React-Bootstrap, and integrates both REST APIs (via Axios/Fetch) and GraphQL endpoints (via Apollo Client).

```
                                  +-------------------+
                                  |    App.js Entry   |
                                  | (Router, Client)  |
                                  +---------+---------+
                                            |
                                  +---------v---------+
                                  |    AppNavbar.js   |
                                  +---------+---------+
                                            |
                  +-------------------------+-------------------------+
                  |                                                   |
                  v                                                   v
        +---------+---------+                               +---------+---------+
        |   Static Components |                               |  Dynamic Components |
        +---------+---------+                               +---------+---------+
                  |                                                   |
  +---------------+---------------+                   +---------------+---------------+
  |               |               |                   |               |               |
  v               v               v                   v               v               v
About.js       Home.js       NotFound.js         Donate.js       CoffeeHome.js    RepoDetails.js
                                              (Apollo Client)                     (Axios REST)
```

### 1. Component Structure and Organization
All frontend source directories are housed within the `frontend/src` path:
- **`App.js`**: Root component of the application. Orchestrates overall routing pathways and declares Apollo GraphQL client initializers.
- **`components/`**: House all page-level view components:
  - **`Home.js` & `About.js`**: Core overview templates with styled callouts.
  - **`ControlledCarousel.js` & `CoffeeCarousel.js`**: Custom interactive image elements displaying project summaries and coffee hobbies.
  - **`RepoDetails.js` & `RepoIssues.js`**: Connect dynamically to Spring REST backend endpoints to parse and render live repository issue lists and code language distributions.
  - **`Donate.js`**: Component that connects directly to the backend GraphQL server to pull permitted charities, handle donations, and filter based on metadata.
- **`utils/activityTracker.js`**: Transmits small, non-blocking telemetry event packets (`click-out` markers) back to the Spring Rate Limiting controller using high-performance sendBeacon/fetch structures.

### 2. State & Data Querying Architecture
The application splits data retrieving strategies cleanly:
- **Scoped GraphQL (Apollo Provider)**: Rather than wrapping the entire application in a heavy GraphQL context, the Apollo Client (`donateClient`) is initialized and dynamically wrapped strictly around the `/donate` Route context. This keeps initial page payloads lightweight and limits unnecessary connection pools.
- **REST Integrations**: Components like `RepoDetails` and `RepoIssues` use lightweight REST querying paradigms to fetch and display repository details.

---

## 4. Data Flow Architecture

The application implements three primary data flow patterns based on the query type (REST vs. GraphQL) and external integration requirements.

```
   [ Client Browser ]          [ REST Controller ]           [ Service Layer ]         [ Cache / Filesystem ]
           |                            |                            |                           |
           |====== POST (Beacon) ======>|                            |                           |
           |   /api/activity/click-out  |                            |                           |
           |                            |--- processClickBeacon() -->|                           |
           |                            |                            |--- updateMetrics() ------>| [ click-out stats ]
           |                            |                            |                           |
```

### Pattern A: REST API Data Flow (e.g., Activity Tracking / Coffee Detail Queries)
1. **Initiation**: The client interacts with a UI component. For example, a user clicks an external link, triggering the `trackClickOut` utility in `activityTracker.js`.
2. **Transportation**: If `navigator.sendBeacon` is available, the client transmits an asynchronous, non-blocking JSON payload to `/api/activity/click-out`. If unavailable, a standard HTTP POST request is triggered via `fetch` with `keepalive: true`.
3. **Ingestion & Rate Limiting**: The request passes through `RequestRateLimitingInterceptor`. The interceptor queries `Bucket4j` rate-limiting tokens based on the caller's IP/profile to prevent Denial of Service.
4. **Controller Routing**: `ActivityTrackingController` receives the payload, validates the input formatting, and forwards it to `ActivityTrackingService`.
5. **Persistence/Action**: The service processes the activity event and logs the metrics accordingly.

---

```
   [ Client Browser ]          [ GraphQL Controller ]         [ Service Layer ]         [ GitHub / AWS API ]
           |                            |                            |                           |
           |======= GraphQL Query =====>|                            |                           |
           |         /graphql           |                            |                           |
           |                            |------ queryDetails() ----->|                           |
           |                            |                            |=== GET (Secured Query) ==>| [ GitHub API ]
           |                            |                            |<== JSON Response =========|
           |                            |<------ mapToDtoList() -----|                           |
           |<==== JSON GraphQL Resp ====|                            |                           |
```

### Pattern B: GraphQL API Data Flow (e.g., Portfolio / Charities / Brews)
1. **Initiation**: The React application initializes or requests specific state. The Apollo Client packages a structured GraphQL query and dispatches it via a single HTTP POST endpoint to `/graphql`.
2. **API Parsing**: Spring GraphQL maps the query fields (e.g., `donateItems` or `brews`) to respective controllers:
   - `DonateGraphqlController` maps charity items.
   - `BrewGraphqlController` maps coffee brew items.
3. **Data Resolving**: The controller invokes the resolver services:
   - `DonateItemsResolver` parses internal static JSON files (such as `json/donate-items.json` and allowed lists from `graphql/charities.txt`) and returns them as mapped schema objects.
   - `BrewsResolver` invokes the cached service.
4. **Response Serialization**: The GraphQL engine formats and returns only the fields requested by the client in a JSON payload.

---

### Pattern C: Third-Party Repository Integration Flow (GitHub)
1. **Authentication**: During initialization, the `AwsSecretManager` calls the AWS Secrets Manager API to fetch the GitHub OAuth token securely.
2. **Scheduled Querying**: A Cron job (`GitDetailsJob`) runs in the background at scheduled intervals.
3. **Remote Fetching**: The `GitDetailsService` uses `GitClient` to query the GitHub REST API (fetching repos, languages, issues, and descriptions).
4. **Caching**: Data retrieved from GitHub is immediately stored in thread-safe, memory-efficient repositories (`GitRepoCacheService` and `GitProjectCacheService`).
5. **Client Request**: When users navigate to `RepoDetails.js` or `RepoIssues.js`, the UI requests repository statistics. The request fetches instantly from the internal cache without hitting GitHub rate limits.

---

## 5. Component Deep Dive

### 1. The Controller Gateway
- **REST Gateway (`net.ironoc.portfolio.controller.*`)**: Handlers for fast transactional posts and static data operations.
- **GraphQL Resolver Gateway (`net.ironoc.portfolio.graph.*`)**: Connects the GraphQL schema definitions directly to backend logic models.

### 2. Caching Layer
- **`GitRepoCacheService` / `GitProjectCacheService` / `CoffeeCacheService`**: 
  - Leverage thread-safe in-memory maps.
  - Implement eviction/clearing via `@PreDestroy` annotations during bean destruction.
  - Mitigate external API call bottlenecks and ensure rapid response times (<50ms).

### 3. Client Layer
- **`GitClient`**: Integrates with GitHub REST services, utilizing robust connection/read timeouts and customized header injectors.
- **`AwsSecretManager`**: Leverages AWS Java SDK v2 to pull credentials dynamically, minimizing hardcoded secret vulnerabilities.

### 4. Routing Layer
- **`PushStateResourceResolver`**: Custom Spring Resource Resolver mapping HTML5 SPA paths back to `/index.html` to keep routing fully browser-native and clean.

---

## 6. Test Standards and Metrics
To guarantee build safety and code correctness, the project enforces strict test coverage limits (**Minimum 80% coverage on all modifications**):
- **Java Backend (JUnit 5, Mockito, Jacoco)**: Maintains an overall instruction coverage of **~88%** and line coverage of **~89%**.
- **React Frontend (Jest, React Testing Library)**: Maintains overall statement and line coverage above **~91%**.

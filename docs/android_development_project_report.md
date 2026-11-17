# Android Development Project — VenueOps

**Project Name:** VenueOps (Venue Management & Booking Platform)  
**Application ID:** `com.aistudio.venueops.vqkzpt`  
**Platform:** Android (Kotlin, Jetpack Compose, Material Design 3, Room Database)  
**Course Alignment:** CSE224 (Fundamentals of Android), CSE225 (Developing Android Apps), CSE226 (Android App Deployment)

---

## 1. Executive Summary

**VenueOps** is a mobile-first Android application designed for event-space owners and operators to manage rentable venues, availability schedules, pricing rules, and booking terms. Built around simplicity, clean MVVM architecture, and strict iterative flow verification, the application uses reactive Kotlin `Flow`/`StateFlow` streams backed by a local **Room Database** with pre-seeded sample venues for immediate inspection and testing.

### Current Implementation Scope
- **Completed & Verified:** **Feature 1 — Event Spaces Management (Owner / Seller)** covering Flows 1.1 through 1.5.
- **Planned Roadmap (Deferred Until Approved):** Feature 2 (Date & Schedule Management), Feature 3 (Customer Space Discovery & Bid Submission), Feature 4 (Bid Management & Negotiation), Feature 5 (Confirmed Booking Management), and Feature 6 (Role Management & Team Access).

---

## 2. Academic Syllabus Alignment

The implementation maps directly to the course outcomes and units across all three Android syllabi:

| Course | Syllabus Units Covered | Implementation in VenueOps |
| :--- | :--- | :--- |
| **CSE224: Fundamentals of Android** | **Unit I (Kotlin & Lifecycle)** | Null-safe Kotlin data models (`EventSpaceEntity`, `OperatingWindow`), sealed navigation state (`VenueScreen`), and `ComponentActivity` lifecycle with `enableEdgeToEdge()` in `MainActivity.kt`. |
| | **Unit II (Compose Layouts)** | Declarative UI using `Scaffold`, `LazyColumn`, `Column`, `Row`, `Box`, `FlowRow`, and `Modifier` chains across `ProviderSpacesScreen`, `EventSpaceCreateEditScreen`, and `EventSpaceDetailScreen`. |
| | **Unit III (State & Input Controls)** | State hoisting and reactive UI controls (`OutlinedTextField`, `Switch`, `FilterChip`, `ExposedDropdownMenuBox`) with field-level validation (`nameError`, `capacityError`, `slotDurationError`, `priceError`, `minBidError`). |
| | **Unit IV (Navigation & Data Transfer)** | Multi-screen single-activity routing with `Crossfade` and `androidx.activity.compose.BackHandler` preserving origin context (`SpacesList` vs `SpaceDetail`) during edit flows. |
| | **Unit V (Feedback & Theming)** | Material 3 `VenueOpsTheme` (`Color.kt`, `Theme.kt`, `Type.kt`), `TopAppBar`, `SnackbarHost` with interactive **Undo** actions, and `strings.xml` branding. |
| | **Unit VI (Permissions & Privacy)** | Zero broad storage permissions (`READ_MEDIA_IMAGES` omitted); uses Android's privacy-preserving Photo Picker (`ActivityResultContracts.PickVisualMedia`). |
| **CSE225: Developing Android Apps** | **Unit I (Views & Components)** | `LazyColumn` cards, `Material 3` chips, status badges, and custom Compose `Canvas` illustrations (`BallroomSampleCover`, `LoftSampleCover`) in `VenueCoverImage.kt`. |
| | **Unit IV (Custom UI Components)** | Reusable domain composables (`SectionCard`, `OperatingWindowCard`, `EventSpaceCard`, `VenueCoverImage`, `ReadOnlyKeyValueRow`) adhering to a strict 3-tier button labelling rule. |
| | **Unit V (Modern Storage & Scoped Access)** | Copies user-picked images via `ContentResolver.openInputStream` into app-internal `filesDir/venue_covers/` so photos persist across restarts without storage permissions. |
| | **Unit VI (Single-Activity Architecture)** | Centralized `MainActivity` hosting all Owner screens cleanly. |
| **CSE226: Android App Deployment** | **Unit I (Advanced Compose & Insets)** | `AnimatedVisibility`, `Crossfade`, edge-to-edge rendering with `navigationBarsPadding()` on bottom action bars, and 48dp minimum touch targets. |
| | **Unit II (Coroutines & Flow)** | `viewModelScope.launch`, `Dispatchers.IO` (`withContext`), and reactive `StateFlow` (`stateIn(SharingStarted.WhileSubscribed(5000))`) in `EventSpaceViewModel` and `EventSpaceRepository`. |
| | **Unit III (MVVM & Repository Pattern)** | Strict separation between Compose UI, `EventSpaceViewModel`, and `EventSpaceRepository` as the single source of truth; lifecycle-safe observation via `collectAsStateWithLifecycle()`. |
| | **Unit IV (Room Database)** | `AppDatabase` (version 2), `EventSpaceEntity`, `EventSpaceDao` (returning `Flow<List<EventSpaceEntity>>`), JSON `Converters` (`List<String>`, `Map<String, String>`), and pre-populated sample venues. |
| | **Unit VI (Quality, Testing & Git)** | Local JVM testing with Robolectric (`ExampleRobolectricTest.kt`), Compose `testTag` instrumentation hooks on all primary actions, and structured `/docs` change tracking for Git/GitHub commits. |

---

## 3. System Architecture & Data Model

### MVVM + Room Persistence Diagram

```text
┌─────────────────────────────────────────────────────────────┐
│                    Jetpack Compose UI                       │
│  ┌───────────────────────┐       ┌───────────────────────┐  │
│  │ ProviderSpacesScreen  │       │EventSpaceCreateScreen │  │
│  └───────────┬───────────┘       └───────────┬───────────┘  │
│              │                               │              │
│              ▼                               ▼              │
│     ┌─────────────────────────────────────────────────┐     │
│     │            EventSpaceDetailScreen               │     │
│     └────────────────────────┬────────────────────────┘     │
└──────────────────────────────┼──────────────────────────────┘
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                    EventSpaceViewModel                      │
│  - Exposes StateFlow<EventSpaceListUiState> & formState     │
│  - Validates form rules (slot max 1440m/24h, min bid <= ptr)│
│  - Handles saveSpace(), setSpaceDisabled(), cover photo URI │
└──────────────────────────────┬──────────────────────────────┘
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                   EventSpaceRepository                      │
│    Single Source of Truth (Room DAO + Sample Venue Seed)    │
└──────────────────────────────┬──────────────────────────────┘
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                      AppDatabase (Room v2)                  │
│  ┌─────────────────────┐         ┌───────────────────────┐  │
│  │   EventSpaceDao     │         │ Converters (JSON/List)│  │
│  └──────────┬──────────┘         └───────────────────────┘  │
│             ▼                                               │
│  ┌───────────────────────────────────────────────────────┐  │
│  │   EventSpaceEntity (Table: "event_spaces")            │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Room Database Schema (`event_spaces`)
Each physical event space is persisted in `EventSpaceEntity` with:
- **Identity & Media:** `id`, `name`, `coverImageUri` (supports picked local images or bundled `sample://` artwork), `tags` (`List<String>`), `description`.
- **Specifications & Location:** `spaceType` (`Indoor`, `Outdoor`, `Hybrid`), `capacity`, `customSpecifications` (`Map<String, String>`), `address`.
- **Availability & Slot Pricing:** `operatingWindowsJson` (list of `OperatingWindow` with `startTime`, `endTime`, and `daysOfWeek`), `slotDurationAmount`, `slotDurationUnit` (`MINUTES` max 1,440 or `HOURS` max 24), `startingPricePerSlot`, `currency`, `unavailableDatesJson`.
- **Bidding & Confirmation Policy:** `isBiddingEnabled`, `minAcceptableBidPerSlot` (private threshold), `isPlatformPaymentsEnabled` (Payment Gate vs. Two-Way Agreement), `refundCutoffDays`, `paymentGateTimerMinutes`, `additionalInfo`, `isDisabled`.

### Pre-Seeded Sample Data
When the database is empty on first launch, `EventSpaceRepository.seedInitialDataIfEmpty()` populates two venues for immediate testing:
1. **Grand Oak Ballroom** — Indoor, Capacity 250, USD 150.0 / 60m, Platform Payments **ON** (`sample://ballroom`).
2. **Downtown Creative Loft** — Indoor, Capacity 45, USD 65.0 / 60m, Platform Payments **OFF** (Two-way Agreement path, `sample://loft`).

---

## 4. Implemented Feature & Flows (Feature 1: Event Spaces Management)

All implemented flows belong to the **Owner / Seller** perspective and follow an explicit **3-Tier Button Labelling Uniformity Rule**:
1. **Compact / Small Buttons** (e.g., TopAppBar actions, cover remove icon): **Icon only**.
2. **1/2 or 1/3 Width Row Buttons** (e.g., `View` | `Edit`, `Cancel` | `Save Changes`): **Words only**.
3. **Full-Width Row Buttons** (e.g., `Choose Cover Photo`, `Edit Event Space`): **Icon + descriptive words**.

### Implemented Flows
- **Flow 1.1 — Browse Owner Event Spaces (`ProviderSpacesScreen`):**
  - Clean vertical card list without Owner-side search or filter clutter.
  - Each venue card displays the space name, address, an immediate **Active/Disabled `Switch` toggle**, **1 cover picture**, key specs (`spaceType`, `capacity`, rate per slot), suitable-use tags, and two equal 1/2-width text-only buttons (`View` and `Edit`).
- **Flow 1.2 — Standard Event Space Creation (`EventSpaceCreateEditScreen`):**
  - 7-section form: (1) Event Space Details + 1-Photo Picker, (2) Specifications + dynamic custom rows, (3) Location, (4) Operating Availability & Time-Slot Sizing, (5) Unavailable Dates, (6) Bidding & Platform Payments, and (7) Additional Information.
  - Enforces validation (required name, positive capacity, slot limit <= 1,440 minutes / 24 hours, non-negative starting price, private minimum bid <= starting price).
- **Flow 1.3 — View Event Space Details (`EventSpaceDetailScreen`):**
  - Strict **read-only mirror** of the exact 7 creation sections and field order, including the 1 cover picture and header Active/Disabled `Switch` toggle.
- **Flow 1.4 — Edit Event Space Configuration (`EventSpaceCreateEditScreen`):**
  - Accessible directly from the card `Edit` button or from the Detail screen's `Edit Event Space` CTA; pre-populates all existing values and updates Room on save.
- **Flow 1.5 — Disable / Re-enable Event Space (`ProviderSpacesScreen` & `EventSpaceDetailScreen`):**
  - Flipping the `Switch` toggle immediately updates Room DB and triggers an **Undo `Snackbar`** confirming marketplace visibility changes while existing confirmed bookings remain intact.

---

## 5. Iterative Review & Documentation Governance

To maintain transparent traceability during development and academic review, four internal markdown files are maintained in `/docs`:
1. `docs/features_and_flows.md` — Master catalog of features, perspectives, screen pathways, and UI rules.
2. `docs/flows_review.md` — Structured log of user feedback and scope clarifications (Round 1 Owner-side review).
3. `docs/review_alterations.md` — Chronological record of all architectural and UI changes made after reviews (Entries 001–004).
4. `docs/confirmed_flows.md` — Verification registry tracking implemented/confirmed flows vs. upcoming features.

---

## 6. Build & Test Verification

- **Build Compilation (`compile_applet`):** Verified clean compilation with KSP (`Room` & `Moshi` codegen) and Coil Compose image loading enabled.
- **Automated JVM & Robolectric Tests (`gradle :app:testDebugUnitTest`):** All unit and Robolectric tests (`ExampleUnitTest`, `ExampleRobolectricTest`) pass (`BUILD SUCCESSFUL`).
- **Environment Stability Fix:** Configured `-Djava.awt.headless=true` in `gradle.properties` (`org.gradle.jvmargs`) to eliminate KSP2 `AWT-EventQueue-0` headless `NullPointerException` warnings during Gradle daemon builds.

---

## 7. Next Milestones (Pending Review & Approval)

1. **Feature 2: Date & Schedule Management (Owner / Seller)** — Add `Schedule` button alongside `View` and `Edit` on venue cards; implement 7-day horizontally scrollable cards, From/To date range search, Day Configuration overrides, and chronological Time Slots with ON/OFF safeguards.
2. **Feature 3: Customer Space Discovery & Bid Submission (Customer)** — Add TopAppBar Owner ↔ Customer mode switcher, Customer Marketplace with search bar + right-end tag filter button, and multi-period bid placement.
3. **Features 4–6** — Bid Negotiation & Proposal Versioning, Confirmed Booking Management, and Role Management & Team Access.

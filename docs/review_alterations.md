# Review Alterations Log

This document records the alterations made to features and flows following user reviews.

---

## Log Entries

### Entry 001 — Initial Project Setup & Lead Feature Definition
- **Date**: Initial turn
- **Trigger**: User instruction to focus on the simplest viable implementation matching course syllabi (CSE224, CSE225, CSE226) and start with the lead feature.
- **Alteration Made**:
  1. Focused the initial implementation on **Feature 1: Event Spaces Management** (Flows 1.1–1.5).
  2. Selected **Room Database** with pre-seeded venue records ("Grand Oak Ballroom" and "Downtown Creative Studio") to allow instant testing and grading.
  3. Established the four-file documentation tracking structure in `/docs`.
  4. Enforced strict single-step execution at the user's explicit instruction.

### Entry 002 — Implementation of Lead Feature (Steps 2 to 5)
- **Date**: Current turn
- **Trigger**: User instruction to "proceed from 3 to 5".
- **Alteration Made**:
  1. Implemented `EventSpaceViewModel` with `StateFlow`, reactive filtering (`All`, `Active`, `Disabled`), text search, and form validation for all 7 specification sections.
  2. Implemented `ProviderSpacesScreen` with status pills, badges, search, filter chips, and "+ New Space" FAB.
  3. Implemented `EventSpaceCreateEditScreen` supporting both initial creation and prefilled editing with validation on slot duration limits (max 1440 min / 24 hrs), custom specification key-value builder, and operating time windows.
  4. Implemented `EventSpaceDetailScreen` with full read-only breakdown of specs, weekly schedules, bidding rules, payment policies, and disable/re-enable dialog.
  5. Connected single-activity navigation in `MainActivity.kt` with `VenueOpsTheme` and `enableEdgeToEdge()`.
  6. Verified compilation cleanly via `compile_applet`.

### Entry 003 — Round 1 Owner-Side Review Alterations (Step 1 & Step 2)
- **Date**: Current turn
- **Trigger**: User confirmed Round 1 Owner-side review assessment and authorized implementing Step 1 and Step 2.
- **Alteration Made**:
  1. **Step 1 (Documentation)**: Updated `docs/features_and_flows.md` with the 3-tier Button Labelling Uniformity Rule, Owner-side removal of search/filter on `ProviderSpacesScreen`, 1 cover picture per space, direct `View`/`Edit` card actions (deferring `Schedule` to Feature 2 and Customer view switcher to Feature 3), immediate `Switch` toggle with Undo Snackbar, and read-only detail mirroring.
  2. **Step 2 (Data & ViewModel)**: Added `coverImageUri: String?` to `EventSpaceEntity`, bumped `AppDatabase` to version `2`, seeded sample cover keys (`sample://ballroom`, `sample://loft`) for pre-populated venues, and added `updateCoverImageUri` plus `setSpaceDisabled` (for immediate toggle + Undo Snackbar support) in `EventSpaceViewModel`. *(Existing `ProviderSpacesScreen` search/filter compatibility members are marked `@Deprecated` for removal in Step 3 so the project remains compilable between steps).*

### Entry 004 — Round 1 Owner-Side UI Implementation & Verification (Steps 3 to 6)
- **Date**: Current turn
- **Trigger**: User instruction `"go up to step 6"`.
- **Alteration Made**:
  1. **Step 3 (Owner Dashboard — `ProviderSpacesScreen.kt` & `EventSpaceViewModel.kt`)**:
     - Removed search bar and `All / Active / Disabled` filter chips from the Owner view (`ProviderSpacesScreen`) and removed `SpaceFilter`/search state from `EventSpaceViewModel`.
     - Added 1 cover picture (`VenueCoverImage.kt`) to every event space card (Coil `AsyncImage` for picked URIs, custom Compose Canvas sample artwork for seeded venues, clean placeholder when unset).
     - Replaced the Disable/Re-enable button with an immediate `Switch` toggle in each card header backed by an Undo `Snackbar`.
     - Applied the 3-tier button labelling rule: direct 1/2-width text-only `View` and `Edit` buttons on each card.
  2. **Step 4 (Create/Edit Form — `EventSpaceCreateEditScreen.kt`)**:
     - Added 1-photo Android Photo Picker (`ActivityResultContracts.PickVisualMedia`) in Section 1 with internal storage persistence (`copyPickedImageToInternalStorage`), small icon-only remove button, and full-width icon+words `Choose Cover Photo` / `Change Cover Photo` button.
     - Switched TopAppBar save action to icon-only (`Check` icon) and kept bottom bar `Cancel` / `Save Changes` as 1/2-width text-only buttons with `navigationBarsPadding`.
  3. **Step 5 (Read-Only Detail Mirror — `EventSpaceDetailScreen.kt` & `MainActivity.kt`)**:
     - Rebuilt `EventSpaceDetailScreen` as an exact read-only mirror of the 7 creation sections and field order, including the header Active/Disabled `Switch` toggle with Undo `Snackbar`, 1 cover picture, and a full-width icon+words `Edit Event Space` bottom button.
     - Wired direct `Edit` navigation from `ProviderSpacesScreen` cards and proper back-navigation state in `MainActivity.kt`.
  4. **Step 6 (Verification)**: Enabled `coil-compose` in `app/build.gradle.kts` and verified clean compilation with `compile_applet`.



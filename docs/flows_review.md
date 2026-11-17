# Flows Review

This document contains the user's reviews, feedback, and notes regarding the flows across all features. Any additions, adjustments, or feedback on specific flows are recorded here.

---

## Status Overview
- **Current Active Feature**: Feature 1: Event Spaces Management (Owner / Seller)
- **Implementation Status**: Round 1 Owner-Side Alterations (Steps 1 through 6) **COMPLETED & COMPILED**.
- **Ready for Review**: Ready for your final verification on the emulator before moving to Feature 2 (Date & Schedule Management).

---

## Feature 1: Event Spaces Management (Owner / Seller)

### Owner-Side Review Items (Round 1):

1. **Button Labelling Uniformity Rule (Owner UI)**:
   - *Very small / compact buttons* (e.g., TopAppBar icon actions): **Icon only**.
   - *1/3 or 1/2 width buttons* (side-by-side buttons in a row): **Words only** (no icon).
   - *Full-width row buttons*: **Icon + descriptive words**.

2. **Flow 1.1 (Browse Spaces — Owner View on `ProviderSpacesScreen`)**:
   - **Remove Search & Filtering from Owner View**: Remove the search bar and `All / Active / Disabled` filter chips from `ProviderSpacesScreen`.
   - **One Cover Picture per Space**: Display **1 picture** on each space card showing what the space looks like.
   - **Disable Control as Toggle**: Replace the `Disable / Re-enable` button with a simple **Toggle (`Switch`)**.
   - **Card Navigation Actions**: Needs confirmation between direct **`View` / `Edit` (/ `Schedule`)** buttons on the card vs. a **`Manage`** button opening a sub-page with **`View`**, **`Edit`**, and **`Schedule`**.

3. **Flow 1.2 (Standard Event Space Creation — Owner View on `EventSpaceCreateEditScreen`)**:
   - Add **1 cover picture** selection for the event space.
   - Enforce the 3-tier button labelling rule on the bottom action bar (`Cancel` / `Create Event Space` or `Save Changes`).

4. **Flow 1.3 (View Event Space Details — Owner View on `EventSpaceDetailScreen`)**:
   - Details page must be a **view-only version** mirroring the exact sections and fields provided when creating the event space (`EventSpaceCreateEditScreen`), including the 1 cover picture.

5. **Flow 1.4 (Edit Event Space Configuration — Owner View on `EventSpaceCreateEditScreen`)**:
   - Accessible from the space card / management options as well as from `EventSpaceDetailScreen`.

6. **Flow 1.5 (Disable / Re-enable Event Space — Owner View)**:
   - Handled via a simple **Toggle (`Switch`)**. *(Needs confirmation: immediate toggle with Undo Snackbar vs. confirmation dialog).*

---

## Deferred Customer-Side Notes (Saved for Feature 3)
- Top App Bar Customer ↔ Owner view switcher button.
- Customer Marketplace (`CustomerMarketplaceScreen`): search bar with filter icon at the right end of the search row, filtering by Event Tags, and 1 picture per space card.

---

## Upcoming Features (Awaiting Approval to Begin)
- **Feature 2: Date & Schedule Management (Owner / Seller)** (`Schedule` destination: 7-day overview, From/To search, Day config override, Time slots with On/Off toggle).
- **Feature 3: Customer Space Discovery & Bid Submission (Customer)**.
- **Feature 4: Bid Management & Negotiation (Owner & Customer)**.
- **Feature 5: Confirmed Booking Management (Owner & Customer)**.
- **Feature 6: Role Management & Team Access (Owner / Seller)**.

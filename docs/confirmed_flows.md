# Confirmed Features and Flows

This document records the features and flows that have been reviewed, verified, and officially confirmed in code.

---

## Confirmed Features & Flows

### Feature 1: Event Spaces Management (Owner / Seller)
*Status: Round 1 Owner-side alterations (Steps 1–6) **IMPLEMENTED & VERIFIED**.*
- **Flow 1.1: Browse Owner Event Spaces (`ProviderSpacesScreen`)** — **IMPLEMENTED & VERIFIED**
  - Clean list of owner spaces without search/filter clutter.
  - Displays 1 cover picture per venue card, Active/Disabled `Switch` toggle in header with Undo `Snackbar`, and 1/2-width text-only `View` | `Edit` buttons.
- **Flow 1.2: Standard Event Space Creation (`EventSpaceCreateEditScreen`)** — **IMPLEMENTED & VERIFIED**
  - All 7 sections including 1-photo Android Photo Picker (`PickVisualMedia`) in Section 1.
  - Obeys 3-tier button labelling rule (small icon-only TopAppBar actions, full-width icon+words photo picker button, 1/2-width text-only bottom bar buttons).
- **Flow 1.3: View Event Space Details (`EventSpaceDetailScreen`)** — **IMPLEMENTED & VERIFIED**
  - Exact read-only mirror of the 7 creation sections and field order, including the 1 cover picture and header Active/Disabled `Switch` toggle.
- **Flow 1.4: Edit Event Space Configuration (`EventSpaceCreateEditScreen`)** — **IMPLEMENTED & VERIFIED**
  - Accessible directly via `Edit` on any venue card or from `EventSpaceDetailScreen`, returning to the originating screen on cancel and to `EventSpaceDetailScreen` on save.
- **Flow 1.5: Disable / Re-enable Event Space (`ProviderSpacesScreen` & `EventSpaceDetailScreen`)** — **IMPLEMENTED & VERIFIED**
  - Instant `Switch` toggle persisted to Room DB with an Undo `Snackbar` noting marketplace visibility change while confirmed bookings remain intact.

---

## Pending Next Features (Awaiting User Approval to Begin)
- **Feature 2: Date & Schedule Management (Owner / Seller)** — *Adds `Schedule` alongside `View` and `Edit` on venue cards; 7-day overview, From/To date search, Day configuration overrides, Time slots ON/OFF.*
- **Feature 3: Customer Space Discovery & Bid Submission (Customer)** — *Adds TopAppBar Customer ↔ Owner switcher, Customer Marketplace with search bar + right-end filter icon button + tag filtering, and multi-period bid placement.*
- **Feature 4: Bid Management & Negotiation (Owner & Customer)**
- **Feature 5: Confirmed Booking Management (Owner & Customer)**
- **Feature 6: Role Management & Team Access (Owner / Seller)**

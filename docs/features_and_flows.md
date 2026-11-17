# Master Features and Flows Catalog

This document defines all features and their independent end-to-end flows for the Venue Management & Booking Platform.

- **Feature**: A broad category of functionality.
- **Flow**: An independent process representing the pathway of pages and interactions required to complete a specific task.
- **Perspective**: Explicitly tagged as **Seller / Provider (Owner)** or **Customer**.
- **UI Button Labelling Uniformity Rule (App-Wide)**:
  1. **Small / Compact Buttons** (e.g., TopAppBar actions, search-row filter icon): **Icon only**.
  2. **1/3 or 1/2 Width Row Buttons** (e.g., `View` | `Edit`, `Cancel` | `Save`): **Text words only** (no icons).
  3. **Full-Width Row Buttons** (e.g., full-width primary action): **Icon + descriptive words**.

---

## Feature 1: Event Spaces Management (Owner / Seller)
*Category: Creation, configuration, listing, editing, and visibility control of rentable physical event spaces.*

### Flow 1.1: Browse Owner Event Spaces
- **Perspective**: Owner / Seller
- **Location**: Home Dashboard (`ProviderSpacesScreen`)
- **Pathway**: App Launch ➔ Owner Dashboard
- **Page Elements & Actions**:
  - Top app bar with app name (`VenueOps`) and venue count badge. *(Customer ↔ Owner switcher deferred until Feature 3).*
  - Clean list of owner event spaces (no search bar or status filter chips on the Owner view).
  - Floating Action Button (FAB) `+ New Space` directing to the creation form.
  - Venue Cards displaying:
    - **1 cover picture** showing what the space looks like.
    - Space name, address, type (Indoor/Outdoor/Hybrid), capacity, starting rate per slot, and **Active/Disabled `Switch` toggle** in the card header.
    - Direct 1/2-width text-only card actions: `View` and `Edit` *(with `Schedule` added alongside them when Feature 2 is built)*.

### Flow 1.2: Standard Event Space Creation
- **Perspective**: Owner / Seller
- **Location**: Creation Form (`EventSpaceCreateEditScreen`)
- **Pathway**: `ProviderSpacesScreen` ➔ Tap `+ New Space` ➔ `EventSpaceCreateEditScreen` ➔ Complete sections ➔ Tap `Create Event Space` ➔ Validation ➔ Saved to Room DB ➔ Return to `EventSpaceDetailScreen`
- **Page Elements & Sections**:
  1. **Event Space Details**: Name (mandatory), **1 Cover Picture** (Android Photo Picker), Suitable Use Tags (FilterChips + custom tag addition), Description.
  2. **Specifications**: Space Type (Indoor/Outdoor/Hybrid), Capacity, and custom dynamic rows (Label + Content with `+ Add Row`).
  3. **Location**: Physical address and optional zone/access notes.
  4. **Operating Availability**:
     - Time Windows: From time, To time, and Mon–Sun day toggles (supports multiple windows).
     - Slot Duration: Numeric amount + unit selector (`Minutes` max 1440, `Hours` max 24).
     - Starting Price per Slot: Numeric amount + currency selector.
  5. **Unavailable Dates**: Multi-date picker listing blocked calendar dates.
  6. **Bidding & Payments**:
     - Bidding Toggle: ON/OFF. When ON, provider enters private Minimum Acceptable Bid per slot.
     - Platform Payments Toggle: ON/OFF. When ON, displays refund cutoff X days, 50% post-cutoff policy, payment gate timer; when OFF, displays two-way agreement note.
  7. **Additional Information**: Venue rules, instructions, equipment notes.
  - **Action Bar**: Bottom bar with 1/2-width text-only `Cancel` and `Create Event Space` / `Save Changes` buttons with validation feedback.

### Flow 1.3: View Event Space Details
- **Perspective**: Owner / Seller
- **Location**: Detail Screen (`EventSpaceDetailScreen`)
- **Pathway**: `ProviderSpacesScreen` ➔ Tap `View` on Venue Card ➔ `EventSpaceDetailScreen`
- **Page Elements & Actions**:
  - **Read-only mirror** of the exact 7 sections and fields entered during Event Space Creation, including the **1 cover picture** at the top.
  - TopAppBar icon-only `Edit` action; bottom bar with Active/Disabled toggle or text-only actions per the 3-tier button rule.

### Flow 1.4: Edit Event Space Configuration
- **Perspective**: Owner / Seller
- **Location**: Edit Form (`EventSpaceCreateEditScreen` pre-filled)
- **Pathway**: `ProviderSpacesScreen` (tap `Edit`) OR `EventSpaceDetailScreen` (tap `Edit`) ➔ `EventSpaceCreateEditScreen` ➔ Modify fields ➔ Tap `Save Changes` ➔ Updated in Room DB ➔ Return to `EventSpaceDetailScreen`.

### Flow 1.5: Disable / Re-enable Event Space
- **Perspective**: Owner / Seller
- **Location**: Venue Card Header (`ProviderSpacesScreen`) or Detail Screen (`EventSpaceDetailScreen`)
- **Pathway**: Flip Active/Disabled `Switch` toggle ➔ Updates Room DB immediately and displays an **Undo `Snackbar`** noting that marketplace visibility is updated while existing confirmed bookings remain intact.

---

## Feature 2: Date & Schedule Management (Provider / Seller)
*Category: Day-to-day schedule overrides, operational slot generation, and slot availability management.*

### Flow 2.1: 7-Day Overview & Date Search Navigation
- **Perspective**: Seller / Provider
- **Location**: Date Management Entry (`DateManagementOverviewScreen`)
- **Pathway**: Provider Navigation ➔ Date Management
- **Page Elements & Actions**:
  - Horizontally scrollable 7-day cards showing date, weekday, Open/Closed status, generated slot count, available slots, confirmed reservations, pending bids, and active payment/agreement holds.
  - From/To Date Range search with quick selections (`Next 7 days`, `This month`, `Next month`, `Custom range`).
  - Selecting any date card opens that date's configuration page.

### Flow 2.2: Day Configuration Override
- **Perspective**: Seller / Provider
- **Location**: Selected Date Page (`DateManagementScreen`, Tab 1: `Day Configuration`)
- **Pathway**: Date Card ➔ Tab 1
- **Page Elements & Actions**:
  - Active/Inactive toggle for the selected date.
  - Operating time windows override.
  - Date-level overrides for Slot duration, Starting price per slot, and Minimum acceptable bid per slot.
  - Clear/Reset override action to restore default space rules.

### Flow 2.3: Time Slots Inventory & On/Off Toggle
- **Perspective**: Seller / Provider
- **Location**: Selected Date Page (`DateManagementScreen`, Tab 2: `Time Slots`)
- **Pathway**: Date Card ➔ Tab 2
- **Page Elements & Actions**:
  - Chronological list of generated slots with status tags (`No bids`, `Pending bids`, `Accepted — Awaiting payment`, `Accepted — Awaiting agreement`, `Accepted — Reserved`).
  - Individual slot ON/OFF toggle with confirmation dialog requiring typing `CONFIRM`.
  - Automatically rejects affected pending proposals or cancels eligible reservations with notifications.

### Flow 2.4: View Slot Proposals List
- **Perspective**: Seller / Provider
- **Location**: Slot Detail Page (`SlotProposalsScreen`)
- **Pathway**: Tap `View details` on a slot card ➔ Lists proposals submitted for that slot ➔ Tap proposal to open negotiation.

---

## Feature 3: Customer Space Discovery & Bid Submission (Customer)
*Category: Discovering venues, checking availability, and submitting multi-period booking proposals.*

### Flow 3.1: Marketplace Listing Discovery
- **Perspective**: Customer
- **Location**: Customer Marketplace (`CustomerMarketplaceScreen`)
- **Pathway**: App Launch (Customer mode) ➔ Marketplace
- **Page Elements & Actions**:
  - Search bar, suitable-use tag filter chips, photo cards with starting price per slot and location.

### Flow 3.2: View Space Details & Rules
- **Perspective**: Customer
- **Location**: Space Details (`CustomerSpaceDetailScreen`)
- **Pathway**: Marketplace Card ➔ Details Page
- **Page Elements & Actions**:
  - Photos, specifications, amenities, location, operating schedule, and booking/refund policies.

### Flow 3.3: Compose & Submit Multi-Period Bid Proposal
- **Perspective**: Customer
- **Location**: Bid Submission Form (`BidSubmissionScreen`)
- **Pathway**: Space Details ➔ Tap `Place a Bid` ➔ `BidSubmissionScreen` ➔ Add Booking Periods (Date, From/To slot, Bid price for period) ➔ Intended Use Info ➔ Answer Provider Questions (Open/Fixed) ➔ Submit
- **Outcome**: Auto-validates against private minimum threshold (auto-rejects below-minimum offers after 2-minute delay with generic notification).

---

## Feature 4: Bid Management & Negotiation (Seller & Customer)
*Category: Proposal review, staged alterations, block-level negotiations, and confirmation gates.*

### Flow 4.1: Provider Proposal Review
- **Perspective**: Seller / Provider
- **Location**: Proposal Review (`ProposalReviewScreen`)
- **Pathway**: Bid Management ➔ Select Proposal ➔ Review terms

### Flow 4.2: Staged Negotiation & Alterations
- **Perspective**: Seller / Provider OR Customer
- **Location**: Negotiation Review (`ProposalReviewScreen`)
- **Pathway**: Propose alteration to date/time/price or propose block removal ➔ Decisions staged in draft response ➔ Tap `Send Alteration` ➔ Immutable new version created.

### Flow 4.3: Per-Block Negotiation History Drill-Down
- **Perspective**: Seller / Provider OR Customer
- **Location**: Block History (`BlockHistoryScreen`)
- **Pathway**: Proposal Review ➔ Tap `View History` on block ➔ Displays actor, timestamp, old value, proposed value, and outcome.

### Flow 4.4: Final Review Accuracy Check
- **Perspective**: Seller / Provider & Customer
- **Location**: Final Review (`FinalReviewScreen`)
- **Pathway**: All terms agreed ➔ Final review verification of complete proposal before gate opening.

### Flow 4.5: Confirmation Gate Execution
- **Perspective**: Seller initiates; Customer completes.
- **Pathway**:
  - *Path A (Platform Payments Enabled)*: Provider opens Payment Gate ➔ Customer pays within timer ➔ Status: `Paid`.
  - *Path B (Platform Payments Disabled)*: Provider sends Agreement ➔ Customer taps `Agree` ➔ Status: `Reserved`.

---

## Feature 5: Confirmed Booking Management (Seller & Customer)
*Category: Post-confirmation records, payouts, cancellations, and claims.*

### Flow 5.1: View Bookings List
- **Perspective**: Seller / Provider AND Customer
- **Location**: Bookings Area (`BookingsListScreen`)
- **Pathway**: Bookings Tab ➔ Filter by `Paid`, `Reserved`, `Cancelled`, `Completed`.

### Flow 5.2: View Read-Only Agreement Snapshot
- **Perspective**: Seller / Provider AND Customer
- **Location**: Booking Record (`BookingSnapshotScreen`)
- **Pathway**: Select booking ➔ View immutable agreed snapshot and payout progress.

### Flow 5.3: Customer Voluntary Cancellation
- **Perspective**: Customer
- **Location**: Booking Record (`BookingSnapshotScreen`)
- **Pathway**: Tap `Cancel Booking` ➔ Shows refund breakdown (post-cutoff: 50% less holding costs) ➔ Confirm cancellation.

### Flow 5.4: Space Unusability Claim & Evidence Submission
- **Perspective**: Customer
- **Location**: Claim Submission (`EvidenceSubmissionScreen`)
- **Pathway**: Tap `Report Unusable Space` (if >=20% time lost due to provider failure and space unused) ➔ Submit evidence within 3-day window ➔ Initiates 100% refund claim.

---

## Feature 6: Role Management & Team Access (Provider / Seller)
*Category: Defining reusable permission sets and assigning team member access.*

### Flow 6.1: Roles Definition Management
- **Perspective**: Seller / Provider
- **Location**: Role Management (`RoleManagementScreen`, Tab 1: `Roles`)
- **Pathway**: Roles Tab ➔ Tap `+ Create Role` ➔ Select permissions (Basic/Advanced) ➔ Save reusable role.

### Flow 6.2: People Invitations & Access Assignment
- **Perspective**: Seller / Provider
- **Location**: Role Management (`RoleManagementScreen`, Tab 2: `People`)
- **Pathway**: People Tab ➔ Tap `Invite Person` ➔ Enter contact info ➔ Assign existing role or create custom role inline ➔ Set venue scope.

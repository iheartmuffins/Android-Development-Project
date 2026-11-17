# Venue Management & Booking Platform — Product Specification

> Living decision document. The **Locked decisions** section contains only items the user has explicitly approved. Items marked **Open / provisional** are design proposals or unresolved details and must not be treated as final requirements.

## Decision process

- Discuss event-space instantiation section by section.
- Only move a decision into **Locked decisions** when the user explicitly says to lock it in.
- Work through one section at a time; move to the next section only after the current one is locked.
- After event-space instantiation is complete, separately define event-space management over time.
- The current product is an Android application. Layouts should be designed mobile-first.

## Product direction

- The primary product is a venue/space management tool for owners and operators.
- Its primary purpose is helping providers manage spaces, monitor day-to-day occupancy, track bookings and see available inventory.
- A secondary marketplace allows customers to discover spaces, check availability and submit booking proposals.
- Public event discovery/advertising is outside the current scope.
- A provider account may manage multiple event spaces. The same account may also use customer functionality; the two modes are not intended to require separate accounts.

## Locked decisions

### A. Booking confirmation paths

1. **Platform payments enabled:** The parties negotiate the booking proposal as usual. The provider opens a payment gate for one selected customer. The payment gate is available only when platform payments are enabled and the provider has completed the required payment configuration. Only one active payment gate may exist for the same or overlapping bookable inventory at a time. The selected customer must complete payment within the configured payment-gate timer.
2. **Platform payments disabled:** No platform payment gateway is opened and the platform does not collect the booking payment. The parties still negotiate the same booking terms. The final confirmation is a two-way agreement: the relevant parties press **Agree** to confirm the negotiated terms.
3. Both paths use the same proposal terms and negotiation history. The difference is the final confirmation mechanism: payment gate versus two-way agreement.

### B. Customer cancellation after the refund cutoff

- The refund cutoff is expressed as **X days before the first day of the booked period**. The value of X is configurable for the relevant policy and remains to be selected.
- When a customer voluntarily cancels **after the refund cutoff**, the customer receives 50% of the booking payment, less applicable holding costs. The other 50% goes to the provider.
- The definition and calculation of holding costs remain open.

### C. Provider-caused failure to provide a usable space

- A customer may demand a full refund when **at least 20% of the total allotted booking time is lost** because the provider did not adequately prepare or provide the actual rented space for use.
- Qualifying problems include the actual rented space being unsafe, customers being unable to enter, or the space being unusable for conducting the function safely or in the intended organised manner.
- The customer must stop and **not use the space** after identifying the qualifying problem. The customer cannot use the space and then claim this full refund under this rule.
- Complaints about the quality of ancillary facilities alone (for example, toilets or other supplementary amenities) do not qualify under this particular rule. The problem must prevent use of the actual rented space itself.
- In a qualifying case, the customer receives a **100% refund**. The provider must return the 50% already paid out to them, and the platform returns the remaining 50% it was holding.
- The customer has **three days after the booked function/booking** to provide evidence of the qualifying provider-caused non-usability.

### D. Proposed staged payout and provider availability confirmation

- On the refund-cutoff day, the platform notifies the provider and asks them to confirm/sign an availability commitment that the actual booked space will be available for the booked period.
- When the provider confirms/signs this commitment, 50% of the booking funds is released to the provider.
- The remaining 50% is held until three days after the function/booking, subject to the evidence/claim process.
- If the provider-caused unusability rule is satisfied, the provider must return the first 50% and the platform returns the held 50% to the customer.

> **Important implementation caveat:** Holding funds, delayed payouts, recovering an already-released payout and issuing refunds depend on the selected marketplace payment provider and applicable jurisdictional requirements. The product rule is locked, but technical and legal feasibility must be verified before implementation.

### E. Date-management navigation and date search

1. The Date Management entry screen shows the **next seven calendar days**, starting with the current date, as horizontally scrollable date cards.
2. Each date card shows the weekday and date, an **Open/Closed** state, total generated slots, available slots, confirmed/reserved slots, and pending bid information. Pending bids should be distinguishable from reservations; where practical, show both the number of bids and the number of distinct slots they concern (for example, “5 bids across 2 slots”).
3. A card may also show a compact operating-hours summary when it fits. If there are many operating windows, the card may shorten the summary; the full windows are available after opening the date.
4. If a payment gate or a no-payment agreement is awaiting customer action, show it separately as a pending/held count rather than including it in confirmed reservations.
5. Selecting a date card opens that date's two-tab page: **Day configuration** and **Time slots**.
6. Date search uses a single date range with **From date** and **To date** fields and a Search action. The Android date picker is used to navigate years, months and days; separate independent year/month/day search systems are not required.
7. A single date is searched by setting From and To to the same date. A month is searched by selecting its first and last dates; a year by selecting 1 January through 31 December; and any other period by selecting its start and end dates.
8. Convenience quick selections are provided for **Next 7 days**, **This month**, **Next month**, **Full year** and **Custom range**. The From/To date range remains the underlying search mechanism.
9. Validate that the From date is not later than the To date.
10. Search results reuse the same daily summary cards. For longer ranges, group the cards under month headings rather than showing an unstructured list of hundreds of dates. A single-date search can open that date directly; a multi-date search displays the daily cards for the selected period.


### F. Confirmed-booking management (initial scope)

The high-level post-confirmation booking-management structure is locked:

1. **Dedicated Bookings area:** Both provider and customer modes have a dedicated place to access their confirmed bookings/reservations. Each party sees the bookings relevant to their role.
2. **Read-only booking record:** Once payment succeeds or the no-payment agreement is completed, the negotiated bid becomes a read-only booking record. The accepted proposal version is preserved as the agreement snapshot; users cannot edit it through the negotiation interface.
3. **Booking details:** The record provides an overview of the booking periods, primary event space, selected additional event spaces, facilities and services, agreed prices, relevant booking information and current status.
4. **Agreement and history:** Users can view the final agreement and read-only negotiation history, including the overall proposal history and the history of individual blocks. Cancellation/refund activity is recorded separately and must not rewrite the accepted proposal or negotiation history.
5. **Payment information:** Payment-enabled bookings expose relevant payment, refund and provider-payout information. No-payment reservations do not display platform payment transactions; any offline financial settlement remains between the parties.
6. **Post-confirmation actions:** The booking-management area supports the relevant cancellation and refund workflows. These are separate actions/processes, not edits to the confirmed booking record:
   - Customer voluntary cancellation.
   - Customer report/request for a refund when the actual rented space was unusable because of provider failure, subject to the previously locked 20% time-loss, non-use and three-day evidence rules.
   - Provider-initiated cancellation, with the applicable customer notification and payment consequences.
   - Cancellation of a no-payment reservation, with notification; any money exchanged outside the platform is settled between the parties.
7. **Initial cancellation scope:** Cancellation applies to the **complete booking**. Partial cancellation of an individual booking period, additional event space, facility or service is deferred from the initial scope.
8. **Post-cutoff customer cancellation:** The already locked rule continues to apply: after the refund cutoff, a voluntary customer cancellation returns 50% of the booking payment less applicable holding costs; the other 50% goes to the provider. The interface must show the applicable outcome before confirmation.
9. **Status distinction:** A successfully paid booking is shown as **Paid**; a completed no-payment agreement is shown as **Reserved**. Other lifecycle states (for example, cancellation pending, refund pending, cancelled and completed) may be displayed as appropriate to the workflow.
10. **Provider payout visibility:** For payment-enabled bookings, show the two-stage payout progress: 50% released after the provider confirms the availability commitment at the refund-cutoff stage, and the remaining 50% held until the three-day post-function evidence period has elapsed, subject to qualifying claims.
11. **No silent record mutation:** Confirmed booking terms remain immutable. Any later cancellation, refund, dispute or future amendment is represented by its own transaction/workflow and audit record.

**Still open within this locked structure:** exact screen layout and labels; customer voluntary-cancellation refund amount before the cutoff; calculation of holding costs; detailed provider-cancellation and dispute handling; evidence review/decision process; and the payment provider/legal feasibility of payout recovery and staged release. Locking the management structure does not resolve these outstanding policy or implementation details.

### G. Event Spaces permission set (Role Management)

The user explicitly approved and locked the following permission structure for the **Event Spaces** section of Role Management. This locks the permission options and their intended scope; it does not lock the full Role Management system or the operational event-space management UI.

**Core permissions**
1. **View event spaces** — view the provider's existing event-space listings and their information.
2. **Create event space** — complete the entire standard event-space creation process, including all mandatory/core information and configurations required to create a usable listing. This permission does not automatically include optional configuration capabilities listed below.
3. **Edit event space** — update the standard/core event-space information and configurations. Optional configuration capabilities remain separately permission-controlled.
4. **Disable event space** — disable the event-space listing's visibility in the customer marketplace instead of deleting it. The provider retains access to manage the disabled event space, its records/history are preserved, and the provider can re-enable it later. Existing confirmed bookings remain valid and are not cancelled merely because the listing is disabled.

**Optional configuration permissions**
5. **Manage customer questions** — create, edit and remove the custom questions customers answer when submitting bids for the event space.
6. **Manage additional event spaces** — link existing event spaces as optional additions to the event-space listing and unlink them.
7. **Manage facilities** — manage which configured facilities are offered as optional additions to the event-space listing.
8. **Manage services** — manage which configured services are offered as optional additions to the event-space listing.

**Permission completeness rule**
- Core Create and Edit permissions must allow the person to complete the full standard operation, rather than granting access to only selected mandatory fields.
- Optional additions (customer-question configuration, linked additional event spaces, facilities and services) are separate permissions and are not prerequisites for completing the standard Create or Edit operation.
- There is no Delete event space permission in this set. Disabling controls marketplace visibility while preserving the provider's records and management access.
- The general Basic/Advanced role-configuration model applies: enabling the Event Spaces section in Basic mode grants the ordinary permissions in this section; Advanced mode allows the specific permissions above to be selected.

**Disable behaviour**
- A disabled event space is no longer visible to customers as an available marketplace listing and should not receive new bids through that listing.
- The provider can still view and manage it and can re-enable it.
- Existing confirmed bookings remain intact.
- The exact handling of already-pending bids or active payment/agreement holds when an event space is disabled remains open and must be defined before implementation.


### H. Bid Management permission set (Role Management)

The user explicitly approved and locked the following permission structure for the **Bid Management** section of Role Management. Apply the complete-operation rule: permissions must grant the actions needed to finish the operation, rather than splitting one workflow into arbitrary optional actions.

**1. View Bid Management — one consolidated read-only permission**
- View incoming bids and their current statuses.
- View complete submitted proposals, including primary booking periods, requested additional event spaces, selected facilities/services, offers, intended-use information and answers to provider-configured questions.
- View block-level and field-level states, current proposal content, submitted versions, overall proposal history and individual block histories.
- View relevant customer/proposal information made available at that stage of the workflow.
- This permission does not allow changing a proposal, submitting responses, or opening a payment/agreement gate. It does not grant early access to contact details that are otherwise withheld.

**2. Manage Bids — one complete provider-side operation**
- Includes the viewing access required to perform bid management; a separate View permission is not required when Manage Bids is granted.
- Review complete proposals and all their blocks, including primary booking periods and optional additional spaces, facilities and services.
- Accept or reject an entire block; accept or alter individual negotiable fields within a block; enter and save replacement values.
- Review and accept or reject the other party's proposed alterations and proposed removals.
- Keep decisions staged in a draft response. Changes/removals are applied to the active proposal only when the response/final review is submitted. If a proposed removal is rejected, the original block remains. If an alteration is rejected, the original value remains.
- Reject the complete bid; prepare and submit counterproposals/final reviews; handle later responses through the same negotiation lifecycle.
- Once the required terms have been agreed and the final review is correct, initiate the configured final confirmation path: open the payment gate when platform payments are enabled, or send/open the no-payment agreement/reservation gate when payments are disabled. Existing timers and inventory-exclusivity safeguards apply.
- View the resulting bid status and hand the confirmed booking to the Confirmed Bookings workflow after successful payment or agreement.

**Boundaries:** Manage Bids does not grant permission to change event-space listing information, customer-question definitions, general/date-specific availability, slot configuration/pricing, or individual slot ON/OFF state; create/edit/delete catalogue definitions for facilities or services; edit payment/refund policies, process refunds or manage payouts; or cancel/amend an already confirmed booking. Those actions belong to their respective permission sections.

**Rules applying to both permissions:**
- The provider's minimum acceptable bid remains private. A below-minimum bid is automatically rejected after the configured two-minute delay, with the generic customer notification: “Your bid has been rejected.”
- Each primary booking period and each optional addition is an independently reviewable proposal block. A proposed block rejection removes the block from the active proposal only after the other party accepts the removal and submits the final review. If the removal is rejected, the original block remains.
- If all primary booking periods are rejected, the entire bid is rejected. Optional additions cannot keep a bid alive without at least one primary booking period.
- Additional event spaces retain their own availability and pricing; selecting one through a primary listing must not bypass its schedule or permit conflicting reservations.
- Submitted proposal versions and history are immutable. Once a booking is confirmed, it is managed under Confirmed Bookings rather than edited through Bid Management.

**Basic/Advanced presentation:** In Basic mode, enabling Bid Management grants both View Bid Management and Manage Bids. In Advanced mode, the provider may grant View Bid Management alone, or Manage Bids for the complete workflow. Manage Bids includes the viewing capabilities it needs.


### I. Role Management administration permission set

The user explicitly approved and locked the following permission structure for the **Role Management** section. Role Management has two administration areas: **Roles** (permission definitions) and **People** (invitations and access assignments). Apply the complete-operation rule: a permission must include all mandatory actions needed to finish the operation; do not split one complete workflow into arbitrary optional actions.

**A. Roles**

1. **View roles** — view saved role definitions, their permission summaries, and the people assigned to each role.
2. **Create role** — complete the role-creation workflow: provide a role name, configure section-level and/or specific permissions using the Basic/Advanced model, review the configuration and save it. Assigning people to the newly created role is a separate People operation and requires the relevant People permission.
3. **Edit role** — complete the role-editing workflow: change the role name and/or permissions, review which currently assigned people will be affected, and save. Because roles are reusable, edits affect everyone assigned to that role; show the impacted people before applying the change.
4. **Delete role** — remove a role definition that is no longer needed. A role currently assigned to people cannot be deleted until those assignments have been moved to another role or otherwise resolved. Deletion must not erase historical actions or permission snapshots.

**B. People**

1. **View people** — view people associated with the provider account, including invitation/active status, assigned role(s), and event-space access scope.
2. **Add/invite person** — complete the invitation workflow, including the required identity/contact information and an initial access assignment. The provider can assign an existing role. Creating a new custom role inline is available only when the acting user also has **Create role** permission; the custom role uses the same role editor and is saved as a named, reusable role, initially assigned to that person.
3. **Change role/access assignment** — change a person's assigned role and/or the event spaces covered by that assignment. The provider can select an existing role. Creating a new custom role during this operation requires **Create role** permission and saves the new role as reusable.
4. **Remove person's access** — revoke or deactivate the person's access. This must preserve their historical actions, proposal decisions, booking records and audit history.

**Permission dependencies and safeguards**
- Manage actions include the viewing access required to complete the operation; a person must not be able to change a role or assignment without being able to view the relevant record.
- Adding a person must result in a defined initial access assignment. If the acting user lacks **Create role** permission, they can assign an existing role but cannot create a custom role inline.
- A person must not be able to grant themselves permissions they do not possess, expand their own access scope, remove the account owner's access, or create a role with authority beyond the acting user's delegated authority.
- Keep role permissions (what a person can do) separate from assignment scope (which event spaces they can do it for). The exact event-space scope/assignment model remains open and is not settled by locking these permission options.
- Deleting a role requires all existing assignments to be resolved first; removal of a person or role must not erase historical actions or permission snapshots.

**Basic/Advanced presentation**
- In **Basic** mode, enabling the Role Management section grants the ordinary operations across both Roles and People: view, create, edit and delete role definitions; view, add/invite, change assignments and remove people's access.
- In **Advanced** mode, expose the eight individual operations above so role-definition administration can be delegated separately from people/invitation administration.
- Inline creation of a new reusable role during Add/Invite or Change Assignment additionally requires **Create role** permission.

## Event-space instantiation — field groups

The creation workflow is intended to collect the sections below. The inclusion and order of the fields discussed so far are recorded here; detailed field behaviour and unresolved choices remain open unless explicitly identified as locked above.

1. **Event-space name / identity** — name used by the provider to distinguish the space.
2. **Event tags / suitable uses** — activities or event types the space can accommodate (for example, tennis).
3. **Images** — pictures of the space; videos are not currently included.
4. **Description and specifications** — a two-column structure: first column is the specification label/category; second column is its content. Include standard rows, then allow custom rows with a provider-entered label and content (for example, “Toilet area” and its description).
5. **Location** — location information for the physical space.
6. **Operating availability and time-slot pricing** — repeatable weekly time-window configurations. Each configuration has a From and To time picker, followed by Monday–Sunday selectors. The provider can add another configuration. A day may be selected in multiple configurations. For any given day and time, if at least one applicable configuration covers that time, the space is open; if none does, it is closed. Multiple configurations covering the same time still mean simply that the space is open. The event space has a **Time slot** field made of a numeric time amount and a time type: minutes or hours. The amount is limited to at most 1,440 minutes (60 × 24) or 24 hours. The provider also enters a public **Starting price per slot** amount and selects its currency. This is the advertised reference price for each generated slot. Weeks, years and day-based slot sizes are excluded from the current proposal.
7. **Unavailable dates** — multi-date picker. Only today and future dates are selectable, up to two years ahead. Selected dates are listed beneath the picker in a readable weekday/date format (proposed example: `Mon-09-Nov-2026`).
8. **Bidding** — an enable-bidding setting and a provider-configured **Minimum acceptable bid per slot** (the minimum-reject threshold). This is a private acceptance threshold, separate from the public starting price per slot. The provider's default threshold applies across dates unless overridden in Date Management. Both the starting price and minimum acceptable threshold use the event space's selected currency. A bid below the configured minimum is automatically rejected after a two-minute delay; the customer is simply notified, **“Your bid has been rejected.”** The notification does not need to disclose the private minimum threshold.
9. **Platform payments and payment policy** — enable platform payments; when enabled, require the payment/refund configuration and show the refund policy, staged payout information and payment-gate timer. When disabled, bookings use the two-way agreement path and no platform payment gate is opened.
10. **Additional information** — supplementary information that does not belong in the structured specifications or other fields.

## Android creation-screen representation

The following is a mobile-first layout representation, not a final visual design. It shows the proposed vertical order and grouping of the fields discussed so far. Fields marked “TBD” need a later decision.

```text
┌──────────────────────────────────────┐
│ ‹  Create event space          Save  │
├──────────────────────────────────────┤
│ EVENT SPACE DETAILS                  │
│                                      │
│ Event space name                     │
│ [ e.g. Main Hall                  ]  │
│                                      │
│ Event tags / Suitable for            │
│ [ Search or select tags           ]  │
│ [ Tennis ] [ Weddings ] [ + Add ]    │
│                                      │
│ Photos                               │
│ [ + Add photos ]                     │
│ [ photo ] [ photo ] [ photo ]        │
│                                      │
│ DESCRIPTION & SPECIFICATIONS         │
│                                      │
│ Specification       Description      │
│ [ Space type     ]  [ Indoor       ] │
│ [ Capacity       ]  [ 100 people   ] │
│                                      │
│ Custom specifications                │
│ [ Label / category                 ] │
│ [ Description / content            ] │
│ [ + Add custom row ]                 │
│                                      │
│ LOCATION                             │
│ [ Address / location details      ] │
│ [ Select location on map           ] │
│                                      │
│ OPERATING AVAILABILITY               │
│ Time window 1                        │
│ From [ 03:00 ]   To [ 07:00 ]        │
│ [Mon] [Tue] [Wed] [Thu] [Fri] ...    │
│                                      │
│ [ + Add availability window ]        │
│                                      │
│ Time slot                            │
│ Time amount [ 30 ]  Type [ Minutes v]│
│ (Minutes max 1,440; hours max 24)    │
│                                      │
│ Starting price per slot              │
│ Amount [             ] Currency [ v ]│
│                                      │
│ UNAVAILABLE DATES                    │
│ [ Select dates                    ]  │
│ Selectable: today through 2 years    │
│ Selected dates:                     │
│ • Mon-09-Nov-2026                    │
│ • Fri-20-Nov-2026                    │
│                                      │
│ BIDDING                              │
│ Enable bidding                 [ON]  │
│ Minimum acceptable bid per slot      │
│ Amount [             ] (private)     │
│ Uses the event-space currency        │
│                                      │
│ PLATFORM PAYMENTS                    │
│ Enable platform payments       [ON]  │
│                                      │
│ If ON:                               │
│ Payment setup / required details     │
│ Refund cutoff: [ X ] days before     │
│ the first day of the booking         │
│ Customer cancellation after cutoff: │
│ 50% back less holding costs          │
│ Payment gate timer: [ duration ]     │
│ Provider confirms availability at   │
│ cutoff; proposed payout: 50% + 50%  │
│                                      │
│ Provider-caused unusability rule:    │
│ ≥20% of booked time lost due to      │
│ provider failure + customer does    │
│ not use the space = full refund.     │
│ Evidence window: 3 days after.       │
│                                      │
│ If OFF:                              │
│ No platform payment gate. Final     │
│ two-way agreement with Agree action.│
│                                      │
│ ADDITIONAL INFORMATION               │
│ [ Other details, restrictions,      ]│
│ [ instructions or notes...          ]│
│                                      │
│             [ Create event space ]   │
└──────────────────────────────────────┘
```

### Android layout recommendations (provisional)

- Use one vertically scrollable creation form with clearly separated section headings/cards. Avoid putting all fields on one dense screen without grouping.
- Keep the primary action (“Create event space”) accessible at the bottom, preferably in a persistent bottom action area once the form is long.
- Use Android-native date/time pickers for time windows and unavailable dates.
- Make weekday selection horizontally scrollable or wrap into two rows on narrow devices; selected days need a clear selected state.
- Each added availability window should appear as its own compact card with From, To, and weekday selectors, plus a remove/edit action.
- When platform payments are disabled, hide payment-only configuration fields and show the two-way agreement explanation instead.
- When platform payments are enabled, reveal required payment setup, refund settings and payment-gate timer. Do not allow the setting to become active until required setup is complete.
- This is a field-layout representation only. Colours, typography, component dimensions, validation messages and final navigation are not yet locked.

## Open / provisional details

- Whether the event-space name is mandatory and whether uniqueness is scoped to one provider account.
- Whether event tags are selected from a managed list, customisable, or both.
- Image count, ordering, cover image and upload constraints.
- The exact standard specification rows and whether a separate general overview paragraph is needed.
- Exact location fields and whether the full address is publicly visible or shared later.
- **Provisional slot-generation rule:** generate only complete slots that fit within available time. Any leftover time that is shorter than the configured slot size remains unavailable; it is not turned into a shorter slot or carried across a closed gap. This remains provisional until explicitly locked.
- The currency-selection behaviour and supported currency list for the price-per-slot field.
- Whether initial unavailable dates are full-day closures only; partial-day exceptions can be considered in later space management.
- Whether the minimum acceptable bid per slot must be at or below the public starting price. Below-threshold bids are provisionally set to auto-reject after a two-minute delay, with only a generic rejection message to the customer.
- The full bidding field set and how disabling bidding affects customer access to the listing.
- Whether the refund cutoff X is configured by each provider or by platform policy.
- How holding costs are calculated and deducted from a customer-cancellation refund.
- What happens when a booking is made after the refund cutoff has already passed.
- What counts as “using” the space for the provider-caused unusability rule, especially brief inspection/entry without conducting the function.
- How the 20% time-loss rule is evaluated when the problem is discovered after the booking period has begun.
- Evidence requirements, provider response rights, who reviews/decides a claim, and what happens if the customer misses the three-day evidence window.
- Whether the final two-way agreement requires one or both parties to press Agree, and when the offline booking becomes reserved.
- Payment-gate expiry behaviour and the duration/unit choices for the timer.
- Payment-provider support and legal/regulatory requirements for staged payouts and payout recovery.
- Exact format of Additional information.

## Section-by-section discussion status

The payment confirmation and refund/payout rules, Date Management navigation/search design, confirmed-booking management structure, and the **Event Spaces permission set** listed under **Locked decisions** are locked at the product-rule/design level. The detailed event-space creation form remains open for section-by-section refinement. Within Date Management, the entry-screen navigation/search and the Date Management permission proposal is approved but not locked; selected-date configuration and time-slot operational behaviour remain open unless separately specified. The Role Management permission options for Event Spaces, Bid Management and Role Management administration are locked under Locked decisions → G, H and I. The Date Management permission structure is an approved working proposal but is not locked. The event-space scope/assignment model for people remains open. No other section should be treated as fully locked merely because it appears in a layout representation.

1. Event-space name / identity — open.
2. Event tags / suitable uses — open.
3. Images — open.
4. Description and specifications — open.
5. Location — open.
6. Operating availability, time-slot size and price per slot — partially specified; detailed behaviour remains open.
7. Unavailable dates — partially specified; details open.
8. Bidding — open. Price per slot is part of operating availability/time-slot configuration.
9. Platform payments, refund policy and payment-gate timer — payment/agreement and refund/payout rules locked; remaining configuration details open.
10. Additional information — open.

## Event-space management over time

Initial working structure now recorded below under **Event-space management over time — working proposal**. The behaviour of each area remains provisional and will be refined separately.

## Change log
- Updated the provisional customer bid form: each repeatable booking-period entry is proposed to include its own bid-price amount, while customer intended-use information is a separate section completed once for the overall proposal. Added provider-configurable public starting price per slot and private minimum acceptable bid per slot at event-space level, with date-level overrides in Date Management. These additions remain provisional.

- Created the living specification and established the explicit-locking workflow.
- Recorded the user's pivot to a venue-management product with a secondary space marketplace; public event discovery is outside current scope.
- Added the proposed event-space instantiation fields, weekly availability configuration, time-slot size and price-per-slot fields, unavailable-date picker, bidding, payments and additional information.
- Added the payment-enabled payment gate and payment-disabled two-way agreement paths.
- Locked the customer-cancellation-after-cutoff rule: 50% returned less holding costs; the other 50% goes to the provider.
- Locked the provider-caused unusability rule: at least 20% of booked time lost due to failure to prepare/provide the actual rented space; customer must not use the space; qualifying case receives a full refund, with the provider returning the first 50% and the platform returning the held 50%; ancillary-facility quality complaints alone do not qualify.
- Locked the three-day post-booking evidence window and the proposed 50%/50% staged payout linked to provider availability confirmation at the refund cutoff.
- Created a mobile-first Android creation-form layout representation. The layout is illustrative; unresolved field behaviour and styling remain provisional.
- Recorded the provisional slot-generation rule: only complete slots fit within available time; leftover time shorter than one slot remains unavailable and is not turned into a shorter slot or carried across a closed gap.
- Clarified day/time availability: for a given weekday and time, if at least one applicable From/To configuration covers that time, it is open; if none does, it is closed. Multiple configurations covering the same time do not create additional availability or require special overlap handling. This clarification is recorded as provisional, not locked.

## Event-space management over time — working proposal

The provider-side management functionality is currently divided into three proposed areas. These are **provisional discussion points**, not locked decisions.

### 1. Date management

Purpose: customise the event space's date-specific and day-to-day availability configuration after the event space has been instantiated.

Initial interpretation:
- The provider selects an event space and views the date-management overview. Each date card has a **Manage** action. Pressing **Manage** opens the management page for that specific date.
- The selected-date management page has the selected date clearly identified at the top and two mutually exclusive tabs (or a tab-style segmented switch): **Day configuration** and **Time slots**. These are navigation controls for switching between two views, not independent on/off settings.
- **Day configuration** shows the selected date's effective configuration and any date-specific overrides. **Time slots** shows the generated slots for the entire selected day. Switching tabs does not change the selected date.
- **Day configuration** contains an Active/Inactive toggle, one or more operating time windows, and date-specific overrides for the time-slot size, public starting price per slot, and (when bidding is enabled) the private minimum acceptable bid per slot.
- The date's configuration is an override of the event space's general availability and pricing defaults. If the provider does not override a setting, the general event-space rule continues to apply.
- Each operating window has a **From** and **To** time picker. Multiple windows can be added for the same date. For example, 07:00–10:00 and 13:00–17:00 means the space is unavailable from 10:00 to 13:00.
- Availability is evaluated simply for each day and time. Check the From/To configurations that apply to that weekday: if at least one configuration covers that time, the space is open; if none covers it, the space is closed. Overlap does not require special handling: a time covered by multiple configurations is still just open once. For example, Monday 07:00–10:00 and Monday 09:00–12:00 means Monday is open from 07:00 through 12:00. The application does not treat one configuration as switching off time covered by another.
- The date-level **Time slot** override uses the same two fields as event-space instantiation: a positive numeric time amount and a time type of **minutes** or **hours**. The maximum is 1,440 minutes or 24 hours.
- The date-level **Starting price per slot** override uses a numeric amount and the event space's selected currency. The amount applies to every generated slot on that date and is the public advertised reference price.
- When bidding is enabled, the provider may also override the **Minimum acceptable bid per slot** for the selected date. This is a private threshold used to reject offers below the provider's configured minimum; it is not displayed as the public starting price.
- If no date-level override is made, the date inherits the event-space's time-slot size, starting price per slot and minimum acceptable bid per slot.
- If the date is inactive, no new bookable inventory is offered for that date. If active, its operating windows and effective time-slot size determine the generated slots.
- Individual generated slots cannot have their own duration or price modifications in this version. Customisation is at the date level.
- Generate only complete slots that fit within available time. Any leftover time that is shorter than the configured slot size remains unavailable; it is not converted into a shorter slot or carried across a time when the space is closed. This remains provisional until explicitly locked.
- **Time slots** shows the bookable inventory generated for the selected date after applying the event-space defaults and any date-specific overrides.
- Each time-slot row/card should show its time range, availability/booking status, and the number of bids or proposals associated with it.
- Selecting a time slot opens a detail view showing the slot's information and the proposals submitted for it. Selecting a proposal opens the full proposal/negotiation view, where the provider can transact with the customer using the agreed negotiation process.
- The list should distinguish at least: available with no proposals, proposals received/pending, provider-accepted but not yet reserved (for example, awaiting payment or the other party's agreement), reserved/confirmed, and unavailable/blocked. The user also wants separate visual tags for an accepted bid that has become reserved and an accepted bid that is not yet reserved. Exact tag wording and the complete bidding-cycle status taxonomy will be defined later.

#### Bid/status tags — user-approved working proposal (not formally locked)

The user indicated that the proposed set below is “good enough” for the bid tags. Keep these as the accepted working labels for the Time Slots display. Per the user's decision process, do not move them into **Locked decisions** until the user explicitly instructs that they are locked.

- **No bids** — no proposals have been submitted for the slot.
- **Pending bids** — one or more proposals are awaiting provider action.
- **Accepted — Awaiting payment** — the provider has selected/accepted the proposal and opened the payment gate, but the customer has not completed payment; the slot is not yet confirmed.
- **Accepted — Awaiting agreement** — the provider has accepted the negotiated terms in the no-payment path, but the required two-way agreement has not yet been completed; the slot is not yet confirmed.
- **Accepted — Reserved** — the booking has been confirmed through successful payment or completion of the two-way agreement.

These labels describe the bid/booking state associated with a slot. They do not replace the independent On/Off availability toggle.

#### Time Slots tab — proposed display and interaction details (provisional)

The Time Slots tab should be a chronological list of all bookable slots generated for the selected date. It should be an operational overview, not a place where the provider edits each slot's duration or price individually.

Proposed page structure:
1. Keep the selected date visible at the top of the page.
2. At the top of the Time Slots tab, show a compact summary of the effective slot size and price per slot for that date (including whether these values are inherited from the event space or customised for this date, if this can be shown without clutter). Since the current model applies one slot size and one price to all generated slots on a date, avoid repeating the same price on every row unless user testing shows that it is useful.
3. List generated slots in chronological order. Each slot row/card should show:
   - Start and end time.
   - An individual **On/Off availability toggle** for that slot. This lets the provider enable or disable that particular generated slot without changing the rest of the day's configuration.
   - A status tag near the time slot, such as **Pending bids**, where relevant. The exact tags for the different bidding/negotiation cycles will be designed later; do not define those cycle labels as part of this section yet.
   - A **View details** action.
4. The On/Off toggle is a per-slot availability control, separate from the date-level Active/Inactive setting and proposal/booking status. Turning a slot Off must first show a warning and require the provider to type **CONFIRM** before proceeding. The consequences depend on the affected proposal/booking state and payment mode:
   - **Pending bids/proposals:** automatically reject every bid/proposal that includes or requires the slot, and notify the affected customers.
   - **Payment-enabled booking that has been paid, while the refund cutoff has not passed:** allow the provider to turn the slot Off only after the warning and typed confirmation. Cancel the affected booking and return the booking payment to the customer. This is a provider-initiated cancellation, not the customer's post-cutoff cancellation policy.
   - **Payment-enabled booking that has been paid, after the refund cutoff has passed:** do not allow the provider to turn the slot Off through this control. The slot's toggle should be unavailable/disabled because the paid booking is protected beyond the refund cutoff. Any exceptional cancellation process is outside this toggle and remains to be defined.
   - **Payment-disabled booking that has been accepted and confirmed through the two-way agreement:** allow the provider to turn the slot Off only after the same warning and typed confirmation. Cancel the reservation/agreement, notify the customer that the accepted bid/reservation has been cancelled, and reject any other bids/proposals that require the slot. No platform refund is involved because the platform did not collect payment.
   - **Payment gate opened but payment not completed:** this is not a paid booking. The proposed handling is to close the active payment gate, cancel/reject the affected proposal, notify the selected customer, and release the hold; confirm this behaviour when payment-gate lifecycle is refined.
   - Turning a slot On makes it available for new proposals only where doing so does not conflict with another active commitment. It does not reinstate previously rejected bids or cancelled bookings.
5. The **View details** action opens a page for that selected slot only. It shows the bids/proposals submitted for that specific slot, not bids for the whole day or other slots. The selected slot's date and time should remain visible.
6. Each bid/proposal is shown as a selectable item. Selecting one opens that customer's full bid/negotiation page, where the provider can review the complete terms and negotiate. The bidding-cycle status labels and exact actions on that page will be designed separately.
7. Back navigation from a bid returns to the selected slot's bid list; back navigation from that list returns to the same day's Time Slots tab and preserves the user's list position.

Design assessment / recommendations:
- Keep the slot list compact and scannable. The provider should be able to identify which periods are free, attracting bids, awaiting customer action, or already reserved without opening each slot.
- Because all slots on a date currently share the same slot size and price, show those values once in a summary rather than repeating identical values in every row. The slot's exact time range remains visible on each row.
- Do not expose the full bid terms directly in the slot list. The hierarchy should be: **date → time slot → proposal → full negotiation**.
- A pending bid does not itself reserve the slot. The slot remains unconfirmed until the relevant payment or two-way agreement process completes. An active payment gate or agreement hold should be visibly distinct from an ordinary pending bid.
- The current design does not include per-slot price/duration editing. Whether providers can manually block an individual generated slot or record an off-platform booking directly from this tab is still an open decision; do not assume that an **Unavailable** or **Occupied** action exists until that is discussed.

Open questions specific to the Time Slots tab:
- Should manual blocking of a single slot and manual entry of an off-platform reservation be available from this tab, or should those actions be designed separately?
- If one customer proposal covers multiple consecutive slots, should it appear against every affected slot or be shown as one linked proposal across the selected range?
- Date-specific overrides take precedence over general recurring rules for that date. Existing confirmed bookings must be protected: changing a date's active state, hours, time-slot size or price must not silently cancel, alter or invalidate an already confirmed agreement.

Recommended screen flow (provisional):
1. Date management → calendar/date selector.
2. Select a date → selected-date screen.
3. Tab 1: **Day configuration** — Active/Inactive, operating time window(s), date-level Time slot (amount + minutes/hours), Starting price per slot (amount + event-space currency), and (when bidding is enabled) Minimum acceptable bid per slot (private threshold).
4. Tab 2: **Time slots** — generated slots with status and proposal count.
5. Select a slot → slot details and all proposals for that slot.
6. Select a proposal → complete proposal and negotiation page.

Design assessment / recommendations:
- The two-tab structure is logical because it separates changing the rules for a date from inspecting the inventory produced by those rules.
- Keep the time-slot list as an overview. Show a proposal count and status there rather than trying to display full proposal details in every row. Put the full proposal cards/list in the selected slot's detail view.
- Reuse the event-space's general rules as defaults and show clearly when a date is using inherited values versus custom overrides. This reduces repeated data entry and makes it easier to understand why a particular date has its current schedule.
- For the first version, do not support editing a single generated slot's price or duration independently. If a provider needs a different slot size or price for a particular date, they change the date-level Time slot or Price per slot override; if they need different operating hours, they change the date-level operating window(s).
- If the selected date is made inactive or its hours are reduced, warn the provider about any existing pending proposals, active payment gates or confirmed bookings that conflict. Do not let a simple date toggle silently invalidate a confirmed booking.
- Treat availability as a simple day-and-time check. For each day, inspect the From/To configurations that apply to that weekday. If at least one configuration says the space is available at a particular time, leave it open; if none does, it is closed. If multiple configurations cover the same time, it remains open once—there is no extra overlap behaviour to manage. Generate slots only in the times that are open. Under the current provisional slot-generation rule, only complete slots are generated; any leftover time shorter than the configured slot size remains unavailable.

Open questions:
- How should the interface show the difference between inherited event-space defaults and date-specific overrides?
- What should happen when a date-level change conflicts with a pending proposal or an active payment gate?
- Exact handling of an active payment gate or an agreement awaiting the other party's confirmation when its included slot is switched Off (the affected proposal is to be rejected; the associated payment/agreement hold must also be closed consistently).
- The individual slot On/Off toggle has state-dependent consequences. Pending bids are automatically rejected. A paid booking can be cancelled through this toggle only before the refund cutoff, with a warning, typed CONFIRM step and return of the booking payment; after the cutoff, the toggle is disabled for that paid slot. A payment-disabled booking confirmed through the two-way agreement can be cancelled through the same warning/typed confirmation process, with customer notification and no platform refund. The provider-initiated cancellation before the refund cutoff returns the booking payment to the customer; payment-provider fee treatment is outside the current product rule.

### 2. Event-space management (the permanent listing/configuration)

Purpose: view and update the event space's persistent information and initial configuration.

Initial interpretation:
- The provider opens an existing event space.
- The application displays the same fields used during event-space instantiation, populated with the currently saved values.
- The provider can edit the initial information, including the space's images and other original fields.
- This concerns the permanent event-space record and its general configuration, rather than a particular dated booking/occupancy record.
- Changes to persistent information or general availability rules must not silently rewrite the terms of existing confirmed bookings.

Design assessment / recommendation:
- Reusing the instantiation form in prefilled edit mode is consistent and avoids maintaining two separate forms.
- The edit page should make clear that changes to general availability rules are defaults for future inventory, whereas date management handles exceptions for particular dates.
- The edit page should also make clear that changing a listing or its general configuration does not rewrite already-agreed booking terms. Any changes that could conflict with pending proposals, active payment gates or confirmed reservations should be surfaced before saving.

Terminology note: use **Event-space management** in the product specification and interface to avoid confusing management of a rentable space with management of an individual event or booking. The user described this as showing the event-space instantiation page again, with its existing fields/values.

Open questions:
- Is this area simply the existing instantiation form in edit mode, or should it also include a read-only details page before entering edit mode?
- Which fields may be edited while the space has pending proposals, active payment gates or confirmed bookings?
- Should changes to listing details be published immediately or saved as a draft for review?
- Are archive, pause-listing and deletion actions part of this area or separate controls?

### 3. Role management

Purpose: define reusable permission sets and assign people access to the provider's event-space operations. The user's latest proposal organises this around **permission sections with specific actions underneath**, plus two distinct administration areas: **Roles** and **People**. This replaces the earlier assumption that role management should be designed only as a generic Basic/Advanced settings page. The Basic/Advanced presentation can still be used when configuring a role, but its final interaction remains open.

#### A. Permission structure — mixed status (see locked permission sets below)

Permissions should be derived from the actual capabilities in the application, not from arbitrary generic labels. Each functional section has specific access actions beneath it. The current proposed starting sections are:

1. **Event spaces** — the exact permission set is locked under **Locked decisions → G. Event Spaces permission set (Role Management)**. It consists of the four core permissions (View, Create, Edit, Disable) and four separately controlled optional configuration permissions (Manage customer questions, Manage additional event spaces, Manage facilities and Manage services). There is no Delete event space permission.
2. **Date management** — the permission structure is an approved working proposal under **Role Management → Date Management permission set**. It consists of one consolidated View Date Management permission, granular child permissions under Manage Day Configuration (including a proposed reset permission), and one complete Manage Individual Time Slots permission covering ON/OFF actions and their consequences/safeguards. It is not locked yet.
3. **Bid management** — a separate top-level permission section, not hidden inside Date Management. Its two permissions (consolidated read-only View Bid Management and complete Manage Bids) are locked under **Locked decisions → H**. Date Management may display bid counts and status indicators, but viewing full proposal contents and acting on bids are controlled by Bid Management.
4. **Role management**
   - Create, view, edit and remove role definitions.
   - Add/invite people.
   - Assign an existing role to a person.
   - Create a custom role during the process of adding a person and assign it to that person.
   - Change a person's assigned role/access.
   - Remove/deactivate a person's access.

**Date Management permission design — approved working proposal (not locked)**

The Date Management permission proposal is recorded under **Role Management → Date Management permission set** as an approved working direction, not a locked decision. It includes one consolidated View permission, granular Manage Day Configuration permissions for each defined configuration operation (including reset), and one complete Manage Individual Time Slots permission with its associated safeguards. Bid Management is also a separate top-level permission section; its two child permissions are locked under **Locked decisions → H**.


#### Date Management permission set — approved working proposal (not locked)

The user agreed with this Date Management permission proposal. It is an approved working direction, but it is not locked because the user has not explicitly instructed that it be locked.

**1. View Date Management — one consolidated permission**
- Grants view access to the complete Date Management experience: the seven-day overview, date-range search and results, date summaries, day configurations, operating windows, time-slot configuration and prices, generated time slots, slot availability/status, and bid-count/status summaries.
- Viewing Date Management does not grant access to full bid contents or bid actions. Those require the separate Bid Management permissions.

**2. Manage Day Configuration — granular permissions**
1. **Set date active/inactive** — enable or disable availability for the selected date as a whole.
2. **Manage operating windows** — add, edit and remove the selected date's From/To availability windows.
3. **Change time-slot configuration** — override the selected date's slot amount and time type.
4. **Change starting price per slot** — override the public starting price for that date.
5. **Change minimum acceptable bid per slot** — override the provider's private minimum-rejection threshold for that date, when bidding is enabled.
6. **Reset date configuration** — clear date-specific overrides and restore the event-space defaults. This is proposed as a distinct specific permission; the user has not yet explicitly locked it.

**3. Manage Individual Time Slots — one complete permission**
- Grants the ability to turn an individual generated time slot ON or OFF.
- Includes the complete associated warning, confirmation, and consequence-handling workflow; it is not split into separate permissions for toggling and handling consequences.
- Turning a slot OFF automatically rejects pending bids that require that slot after the provider confirms the warning.
- For a paid booking before the refund cutoff, the action follows the applicable cancellation/refund process and returns the customer payment under the agreed rule.
- A paid booking cannot be turned OFF after its refund cutoff has passed.
- For an accepted no-payment reservation, the provider can cancel it through the confirmation process and the customer is notified.
- Active payment/agreement holds and overlapping inventory must be handled according to the booking safeguards; this permission cannot bypass those safeguards.

**Permission completeness and separation rules**
- The general Basic/Advanced role-configuration model applies. In Basic mode, enabling Date Management grants the ordinary permissions in this section. In Advanced mode, the provider can select the specific permissions above.
- **View Date Management** is one consolidated permission; it is not split into calendar, configuration and slot-list viewing permissions.
- **Manage Day Configuration** is the only part of Date Management broken into granular configuration actions.
- **Manage Individual Time Slots** remains one complete permission covering ON/OFF and the associated consequences/safeguards.
- Bid counts and status summaries may be visible under View Date Management, but full proposal details and proposal actions belong to the separate **Bid Management** section.

#### Bid Management — locked permission section

Bid Management is a separate top-level permission section, not a child of Date Management. Its locked child permissions are View Bid Management (one consolidated read-only permission) and Manage Bids (one complete provider-side operation), as specified under Locked decisions → H. Date Management may display bid counts and status summaries, but full proposal contents and proposal actions are controlled by Bid Management.

**Additional permission domains that still need a clear place**

The first three sections above reflect the user's current framing, but the application also contains capabilities that do not naturally belong inside Event spaces or Date management. Before the permission catalogue is finalised, assess whether these should be separate access sections:
- **Proposals and negotiations:** view proposals and customer answers; accept/alter/reject blocks; reject an entire bid; submit a counterproposal/final review; initiate the relevant payment or agreement gate.
- **Confirmed bookings:** view confirmed booking records and agreement/history; initiate permitted booking cancellation actions.
- **Payments, refunds and payouts:** view financial records; manage payment/refund configuration; process permitted refunds or payout-related actions. These are sensitive and should not be granted merely because someone can manage an event space or date.

Facilities and services may be grouped under Event spaces if the provider regards them as part of configuring what a space offers. Their catalogue create/edit/delete permissions should remain individually identifiable beneath that section if the owner wants to delegate only those operations. Additional event spaces linked to a primary listing should likewise be distinct from editing the linked event space's own details or schedule.

#### B. Basic and fine-grained access — working proposal

- A section-level access control can grant all ordinary actions underneath that section.
- If the provider selects only specific actions beneath a section, only those selected actions should be granted; the other child actions remain unavailable.
- The UI should distinguish **Off**, **Full access** and **Custom access**.
- If the Basic/Advanced selector is retained: Basic shows only section-level controls; Advanced exposes the child actions. Enabling a section in Basic grants all normal child capabilities. In Advanced, the provider can select individual child capabilities. Switching modes must preserve the current permissions rather than unexpectedly resetting them.
- High-impact operations (for example, deleting/archiving an event space, rejecting an entire bid, cancelling a confirmed booking, issuing a refund, changing payment configuration, or administering other people's access) must be clearly identifiable. Whether some require separate explicit permissions or additional safeguards remains open.

#### C. Two distinct areas inside Role management

**Roles area**
- Displays the permission roles already created by the provider.
- The provider can create a role, give it a name, configure its section-level and/or specific permissions, and save it.
- After creating a role, the provider can search for/select people to assign that role to. Assignment can also happen later from the People area.
- A role definition is reusable; changing a role may affect everyone assigned to it. The interface should show who is assigned before a consequential edit or deletion.

**People area**
- Displays people who have been invited/added and their assigned access.
- The provider can add/invite a person and then either assign an existing role or create a custom role for that person during the same flow.
- A custom role created during the add-person flow should use the same role/permission structure as a normally created role. A design choice remains whether it is automatically saved as a reusable role or can be a person-specific permission set. The simpler, more maintainable recommendation is to save it as a named role, initially assigned to that person, so it can be reused later.
- The provider can change a person's assigned role or remove/deactivate their access. Revoking access must not delete that person's historical actions, proposal decisions or booking records.

#### Role Management permission set — locked

The Roles and People areas and their eight child permissions are locked under **Locked decisions → I. Role Management administration permission set**. The exact event-space access-scope/assignment model remains open; locking the permissions does not decide which event spaces a particular person's role assignment covers.

#### D. Scope of access — working recommendation

Keep **what a person can do** separate from **which event spaces they can do it for**. A role defines the actions; when assigning it to a person, the provider should be able to choose the event spaces covered by that assignment. For example, one person could manage dates for Hall A but only view Hall B. This is a recommended model, not yet locked.

#### E. Critical design recommendations

- Derive child permissions from real application actions. Avoid a generic Create/Edit/Delete set where the domain needs specific actions such as Accept, Alter, Reject, Turn slot OFF, Cancel reservation or Issue refund.
- Do not automatically grant proposal, booking or financial permissions just because a person can access Event spaces or Date management.
- Make it clear when editing a reusable role affects multiple people.
- Prevent delegated users from granting themselves permissions they do not possess, removing the owner's access, or expanding their own event-space scope.
- Require a viewing permission or minimum record visibility for actions that need access to the underlying record, but do not use this dependency to grant unrelated capabilities.
- Deleting/archiving event spaces, facilities or services must not erase confirmed booking snapshots or audit history.

#### F. Open questions to resolve section by section

- Are the top-level permission sections exactly Event spaces, Date management, Role management, or should Proposals/negotiations, Confirmed bookings, and Payments/refunds be additional top-level sections? (Recommendation: keep those three operational domains separate rather than hiding them under Event spaces or Date management.)
- The Event Spaces permission set is locked under **Locked decisions → G**. The behaviour of pending bids/active holds when an event space is disabled remains open.
- The exact event-space scope/assignment model for people remains open (for example, whether a person can have different permission roles for different assigned spaces).
- Date Management permissions remain an approved working proposal, not locked; its individual operation list and safeguards are recorded separately.
- Under Role management, should creating/editing/deleting role definitions and inviting/removing people be separate child permissions? (Recommendation: yes.)
- When adding a person, should a custom role be automatically saved as a reusable named role or remain unique to that person?
- Can the same person receive different roles for different event spaces? Can a person hold more than one role for the same event space?
- Is resource scope assigned at the person-role assignment level (recommended), or embedded in the reusable role itself?
- Who may create/edit roles and assign access? Should only the account owner/admin be able to grant Role management permissions?

### Management structure status

The three proposed management areas are recorded for discussion:
1. Date management — date-specific availability and schedule customisation.
2. Event-space management — view/update the permanent event-space record using the instantiation fields.
3. Role management — manage reusable role definitions and people assignments, with section-based and specific permissions.

The latest Role Management proposal has two administration areas (**Roles** and **People**) and derives permissions from the application's actual operations. The **Event Spaces** permission set is locked under **Locked decisions → G**. The Roles/People administration permission options are now locked under Locked decisions → I. The exact resource scope/assignment model remains open. The Event Spaces and Bid Management permission sets are locked; the Date Management permission proposal is approved but not locked. The Date Management entry-screen navigation and date-search design is locked under **Locked decisions → E**. Other Date Management behaviour and the operational Event-space management workflow remain open.

## Change log (additional)

- User indicated the proposed bid/status tags are good enough as the working set: No bids; Pending bids; Accepted — Awaiting payment; Accepted — Awaiting agreement; Accepted — Reserved. These remain provisional until the user explicitly says to lock them.

- Locked the Date Management navigation and search design: next seven days as horizontally scrollable daily cards with operational summaries; From/To date-range search with quick selections; longer results grouped by month; selecting a date opens Day configuration and Time slots. Exact card information is recorded under Locked decisions → E.

- Began defining the provider's ongoing management functionality as three provisional areas: Date management, Event-space management and Role management. No new management decisions have been locked.
- Added the user's provisional Date management proposal: selected-date page with Day configuration and Time slots tabs; date-level active state, operating hours and price rule; generated slots with status/proposal counts; slot details leading to the full proposal/negotiation view.
- Added design recommendations for separating date-level configuration from generated inventory, inheriting general rules unless overridden, and protecting pending/confirmed bookings from conflicting changes.
- Clarified the event-space and date-level time-slot model: slot size is a numeric amount plus minutes/hours (maximum 1,440 minutes or 24 hours); price is a numeric amount plus currency and is charged per generated slot. Date management can override the slot size and price for a selected date, while inheriting event-space defaults when no override is made.
- Expanded the Event-space management proposal as the instantiation form reused in prefilled edit mode.
- Added the proposed Basic/Advanced permission setup for Role management, with recommendations to separate resource scope from action permissions and isolate sensitive capabilities.
- Refined Role management as a permission tree derived from actual product operations. The current working sections begin with Event spaces, Date management and Role management, each with specific child actions; Proposals/negotiations, Confirmed bookings and Payments/refunds are flagged as additional domains that may need their own top-level permission sections.
- Defined two distinct Role management areas provisionally: Roles (create and configure reusable roles, then assign people) and People (add/invite a person, assign an existing role or create a custom role during the flow, change access or remove a person). Recommended that custom roles use the same reusable role model, and that action permissions remain separate from the event spaces to which they apply. No Role management decisions have been locked.

- Added a provisional Time Slots tab layout: chronological slot list, date-level slot-size/price summary, and per-slot availability toggle. Each slot has a status tag (specific bidding-cycle tags deferred for later definition) and a View details action. View details shows bids for that selected slot only; selecting a bid opens its full negotiation page.
- Expanded the provisional individual-slot Off behaviour: pending bids/proposals that include the slot are automatically rejected; a paid booking can be cancelled by the provider through the toggle only before the refund cutoff, with a warning, typed CONFIRM step, customer notification and return of the booking payment; after the refund cutoff the toggle is disabled for that paid slot. A payment-disabled booking confirmed through the two-way agreement can be cancelled through the same warning and typed confirmation process, with customer notification and no platform refund. The provider-initiated cancellation before the refund cutoff returns the booking payment to the customer; payment-provider fee treatment is outside the current product rule. A payment gate that has opened but has not been paid is proposed to close and release its hold if the slot is turned Off; this detail remains to be confirmed.
- Clarified the provider's proposed Time Slots interaction: each generated slot has its own On/Off availability toggle; a nearby tag indicates relevant status such as pending bids; the user wants accepted-but-not-yet-reserved and accepted-and-reserved states distinguished; exact bidding-cycle tag labels will be defined later; View details opens the bid list for that slot only; selecting a bid opens the bid/negotiation page. This remains provisional and has not been locked.

### Date management navigation and date search — locked

Purpose: let the provider quickly review upcoming daily operations, or find a particular date / date range without creating a complicated custom year-month-day search system.

Locked entry screen:
- Show the next seven calendar days by default, starting with the current date.
- Present the seven dates as horizontally scrollable cards.
- Each card should show the weekday, day/date, and a simple operational state such as Open or Closed.
- Each card should summarise the date's generated slot inventory: total slots, available slots, and confirmed/reserved slots.
- Show outstanding bids separately. Where practical, display both the number of pending bids and the number of distinct slots those bids relate to (for example, “5 bids across 2 slots”), because multiple bids can exist for one slot and bids do not themselves reserve that slot.
- If a payment gate or no-payment agreement is awaiting the customer's action, show that as a separate pending/held count rather than mixing it with confirmed reservations.
- Optionally show a compact operating-hours summary on each card. If there are many windows, the card can show the number of windows or a shortened summary; full windows are visible after selecting the date.
- Selecting a date card opens that date's existing two-tab page: Day configuration and Time slots.

Locked search behaviour:
- Use a single date-range search with two Android date-picker fields: **From date** and **To date**, followed by a Search action.
- The Android calendar/date-range picker already supports navigating by year, month and day. There is no need to create separate independent year, month and day search systems.
- A single date is searched by setting From and To to the same day.
- A whole month is searched by selecting its first and last dates.
- A whole year is searched by selecting 1 January through 31 December of that year.
- A custom period is searched by choosing any start and end dates.
- Suggested quick selections: Next 7 days, This month, Next month, Full year, and Custom range. These are convenience shortcuts; the From/To range remains the underlying search mechanism.
- Validate that the From date is not later than the To date.
- Search results should reuse the same daily summary cards. For long ranges, group the daily cards under month headings (which can be collapsed/expanded) rather than presenting an unstructured list of hundreds of days.
- A search for one date can open that date directly; a search for multiple dates displays the daily cards for the selected period.

Locked card summary:
1. Date and weekday.
2. Open/Closed state.
3. Operating-hours summary, where it fits.
4. Total generated slots.
5. Available slots.
6. Confirmed/reserved slots.
7. Pending bids, ideally expressed as bids across distinct slots.
8. Payment-pending or agreement-pending holds, if any.

The user explicitly approved this date-navigation and search design and then instructed that it be locked. The decisions are recorded under **Locked decisions → E. Date-management navigation and date search**. Other Date Management behaviour (including the detailed Day configuration and Time slots interactions) remains open unless separately locked.

## Customer-side space discovery and booking proposal — working proposal

The customer flow is being designed to define what information is included in a bid/proposal. These points are provisional and are not locked.

### Proposed customer journey
1. **Discover spaces:** The marketplace presents event-space listings (not public event advertisements). A listing card should help customers identify the space, its suitable-use tags, location, photos and indicative starting price.
2. **View event-space details:** Selecting a listing opens the full event-space information: name, photos, suitable-use tags, description/specifications, location, availability information, additional information and applicable booking/payment arrangements.
3. **Place a bid:** The customer selects the dates and actual bookable time slots they want, supplies information about their intended use, and submits a price proposal.
4. **Negotiate:** The provider reviews the proposal for the affected slot(s). Selecting a proposal opens the full proposal/negotiation page. A submitted bid is not itself a confirmed reservation.
5. **Confirm according to the provider's payment setting:** The event-space owner's configuration determines the final confirmation path. If platform payments are enabled, the provider may open the payment gate for the selected customer after negotiation. If platform payments are disabled, the parties complete the two-way agreement by pressing Agree.

### Recommended bid-form structure

**A. Requested dates and times — revised working proposal**
- Use a repeatable **booking period** entry rather than one date-range form that assumes the same time window across every selected date.
- Each entry represents one calendar date and one continuous From/To period on that date.
- The customer selects a date using the calendar, then selects a start and end time from the actual availability generated for that date. These choices must align with the event space's configured slot boundaries; the customer is selecting existing inventory, not inventing arbitrary time intervals.
- The app derives the requested slots, date, start time, end time and duration from that selection.
- The customer presses **Add** to add the date/time period to the proposal. They can repeat this process to include additional dates, each with its own time period. This supports bookings across multiple days without requiring a multi-date calendar selection that applies one repeated schedule to every day.
- Show each added period as a summary row/card (date, From–To time, number of slots and starting-price subtotal), with an option to remove it before submitting.
- All periods added in this way belong to one booking proposal. The proposal references all selected slot inventory, and the complete set of periods is negotiated and confirmed together.
- For a simpler first release, recommend one continuous period per entry. Whether to allow more than one separate period on the same date remains open; the simplest initial rule would be one entry per date.
- If any required slot is subsequently disabled, the proposal is affected and follows the slot-off rejection/cancellation rules. Submission must revalidate all selected slots to ensure none became unavailable while the customer was composing the proposal.

**A1. Booking-period entry fields — updated working proposal**
- Each repeatable booking-period entry contains: **Date**, **From**, **To**, and **Bid price for this period**.
- The date and From/To values must select existing available generated slot inventory. The system derives the selected slot count, duration and advertised starting-price subtotal.
- The customer enters the proposed amount for that period and presses **Add**. The added period is then shown in the proposal summary with its date, time range, slot count, starting-price subtotal and customer offer. The customer can add further date/time periods, each with its own offer.
- The same proposal may contain multiple booking periods. For the simpler first release, retain the recommendation of one continuous period per date; whether to support multiple separate periods on the same date remains open.

**B. Intended use / customer information (separate from booking periods)**
- This is a separate section of the bid form, completed once for the overall proposal; it is not repeated inside each date/time booking-period entry.
- Proposed core fields: **Activity/event name**, **intended activity/type**, and a **description of how the customer plans to use the space**.
- Other useful information the provider may need to assess the bid includes expected attendee/participant count, setup requirements, equipment/facility needs and any special arrangements. The exact fields and whether they are mandatory remain open.
- The provider may eventually be able to define additional questions or required information for customers, but this is not yet a decided feature.
- The information should help the provider assess suitability and prepare for the proposed use. It remains distinct from the permanent event-space description/specifications.

**C. Price offer — revised working proposal**
- Each booking-period entry includes its own **Bid price** field, entered before the customer presses **Add** to include that period in the proposal.
- The customer therefore supplies one offer amount for each date/time period, rather than entering one undivided amount for the entire multi-date proposal.
- For each period, show the provider's public starting price per slot, the number of generated slots selected, and the calculated starting-price subtotal. The customer enters the amount they are offering for that complete period.
- All periods in the same proposal are summed to show the customer's overall proposed amount. Each period retains its own offer and price breakdown, which supports different date-level prices and minimum thresholds.
- The bid amount uses the event space's currency. Currency is inherited from the listing; the customer does not select a different currency.
- The provider's **Minimum acceptable bid per slot** is a private threshold. The system compares each booking-period offer with the minimum acceptable total for that period (minimum acceptable bid per slot multiplied by the number of requested slots). If a submitted bid is below the applicable minimum, the system waits two minutes, then automatically rejects it and tells the customer only: **“Your bid has been rejected.”** The private threshold is not disclosed. This rule applies to a below-minimum booking-period offer; how the platform handles a proposal containing several periods when only one period is below its minimum remains to be clarified if multi-period proposals are retained.
- This per-period offer model supersedes the earlier provisional recommendation to enter one total bid amount for the entire proposal.

**D. Payment / reservation path**
- Do not make payment versus reservation an independent customer choice if the provider has already selected one payment mode for the event space. The provider's setting controls the final confirmation path.
- If platform payments are enabled, the customer may be asked to pay only after the provider opens a payment gate for them. The bid form should explain that payment is not taken at initial bid submission.
- If platform payments are disabled, the bid proceeds through negotiation and the two-way Agree confirmation; no platform payment gate is shown.
- Whether the customer should be shown an upfront informational label such as “Online payment available” or “Payment arranged directly with provider” remains a presentation decision.

### Design assessment
- Do not ask customers to enter “number of days”, “hours” or “minutes” as a separate booking duration when the provider's actual inventory is already divided into bookable slots. Selecting the actual slots is clearer and prevents requests that do not align with availability.
- Derive requested dates, times and duration from the selected inventory. This also makes slot conflicts and proposal-to-slot relationships explicit.
- Keep intended-use details separate from the permanent event-space description. The former describes this customer's proposed booking; the latter describes the venue itself.
- A bid is a proposal, not a reservation. The slot remains unconfirmed until the provider/customer confirmation path is completed according to the provider's payment setting.
- Because one proposal can potentially require multiple slots, retain a single proposal identity and link it to all affected slots rather than creating unrelated copies of the bid.

### Open questions
- Should the first release allow only one booking-period entry per date, or allow multiple non-overlapping periods on the same date? The simpler recommendation is one entry per date.
- Should each period's From/To selection be made through two constrained time pickers, or by tapping/selecting consecutive generated slots? In either case, only available slot boundaries may be selected.
- The current working proposal is one bid amount per booking-period entry, summed for an overall proposal total. The minimum threshold is compared against each period's required minimum total; the handling of a multi-period proposal where only one period is below its threshold remains open.
- Is attendee/participant count required, optional, or omitted from the initial bid form?
- What exact intended-use/customer-information fields should be required beyond an activity/event name, activity type and description? Should attendee count, setup requirements and equipment needs be mandatory, optional or omitted?
- Should providers be able to define additional questions that customers must answer when bidding?
- For a proposal containing multiple booking periods, should one period falling below its minimum threshold reject the entire proposal after the two-minute delay, or only reject that booking period? The customer currently receives the generic rejection message and is not shown the provider's private minimum.
- Should the private minimum acceptable bid per slot be required to be at or below the public starting price per slot? The recommended validation is that it cannot exceed the public starting price.
- What should happen if a customer-selected slot becomes unavailable while the customer is filling out the bid form? The submission must revalidate all selected slots before accepting the proposal.
- Added a provisional customer discovery and bid-submission journey. Recommended selecting actual generated slot inventory rather than manually entering arbitrary duration fields; separate intended-use details from event-space details; price offer proposed as a total for the complete proposal; and the provider's payment setting determines payment-gate versus two-way-agreement confirmation. Multi-day booking and exact bid fields remain open.

- Revised the provisional customer booking form to support multiple dates through repeatable booking-period entries. Each entry is one date plus one continuous From/To period selected from generated available slot inventory. The customer uses Add to include further dates/time periods in the same proposal. This replaces the earlier broad date-range idea as the preferred design direction; no multi-date calendar selection with a common repeated time window is currently proposed. Whether multiple periods on the same date should be allowed remains open.


- Clarified the private minimum-bid rejection behaviour: when a submitted bid is below the provider's configured minimum, the system waits two minutes and then automatically rejects it. The customer receives only the message, ‘Your bid has been rejected’; the private minimum threshold is not disclosed. This remains a working decision unless explicitly locked.

## Customer information requirements and bid negotiation — working proposal

> This section records the current design discussion only. It is provisional and must not be treated as locked until the user explicitly says to lock it in.

### 1. Two distinct information groups

The bid should distinguish between **customer information** (what the provider needs to know to assess and prepare for the requested use) and **negotiable agreement terms** (what the provider and customer must ultimately agree on).

#### A. Standard customer information

The bid form should contain a small set of common fields completed once for the overall proposal. The current working examples are:
- Activity/event name.
- Intended activity/type.
- Description of how the customer plans to use the space.
- Expected attendee/participant count (whether this is required or optional remains open).
- Setup, equipment, facility or special-arrangement requirements (exact structure remains open).

#### B. Provider-defined questions — current working design

Proposed feature: allow the provider to create additional questions that customers answer when submitting a bid for the relevant event space. These questions collect space-specific information that is not covered by the standard bid form.

The proposed Android question builder has two tabs:

**1. Open field**
- The provider enters the question.
- The provider selects the answer type: **Short text**, **Long text**, or **Number**.
- The customer responds by entering their own answer.

**2. Fixed field**
- The provider enters the question.
- The provider adds the available answer options (the fixed answers from which the customer will choose).
- The provider specifies whether the customer may select **one answer** or **multiple answers** from the available options.
- The customer selects from the provider's predefined options rather than writing a free-form answer. The response control is single-select or multi-select according to the provider's setting.

**Common controls and question list**
- Each question can be marked **Required** or **Optional**.
- The provider adds a completed question to the question set.
- The builder displays the questions already created for that event space.
- The provider can remove a question from that list.
- Fixed fields include a setting that determines whether the customer may select one answer or multiple answers.

Proposed placement and use:
- Configure the question set in the management of the relevant event space, rather than making customers or providers build questions separately for every bid.
- The customer sees the configured questions in a separate additional-information section of the bid form and answers them once for the overall proposal, not once per booking-period entry.
- Store the question wording, field type/options, required setting and customer's answer as a snapshot with the submitted proposal. Later edits or removal of a question from the event space must not rewrite historic bids.
- The provider reviews the answers before deciding whether to negotiate or reject the proposal.

Design assessment: the two-tab Open/Fixed builder is a good constrained alternative to an unrestricted sandbox. It covers free-form answers and predefined choices without requiring a complex form-builder platform. The question builder design is still provisional until explicitly locked.

#### C. Additional event spaces, facilities and services — current working proposal

The user later revived and developed this feature. It is no longer treated as shelved. The current design remains provisional:
- **Additional event spaces:** A provider can link other event spaces already configured in their account to a primary event-space listing. Customers can optionally request one or more linked spaces as part of the same proposal. A linked space follows the primary booking periods for dates/times, but retains its own availability and pricing. The customer supplies a separate offer for the linked space. Availability of every selected space must be validated before confirmation; overlapping use of the same resource must be prevented.
- **Facilities and services:** The provider can create reusable account-level catalogue entries in separate Facility and Service categories. A proposed entry contains a name, images, a custom two-column specification/description table, and pricing configuration. The proposal for pricing has two modes: **Time rate** (price amount plus time amount and time type) or **Fixed rate** (one price for the booking). Both modes may have a public starting price and a private minimum acceptable amount; exact calculation and validation rules remain open.
- A primary event-space listing can expose selected catalogue facilities and services as optional additions. Customers can inspect an item's details, switch to its Offer view, turn it on for the proposal, enter an offer and save it. Details and offer configuration are separate views for the currently selected item.
- Optional additions are not compulsory. Rejecting an additional event space, facility or service does not invalidate the primary booking proposal.
- These optional additions are intended to be negotiable proposal blocks. Their inclusion/removal is handled at block level; their price offer is negotiable at field level. Descriptions and specifications are informational, not editable negotiation terms.

The user's intended Android interaction for the optional-item selector is a two-column layout on wider screens: the first column is an item selector (one row per available item), and the second/main column displays the selected item's content. A Details/Offer selector changes the main content for that item. On narrow phones, the same interaction may reflow to an item selector above the content rather than forcing a cramped side-by-side split. Exact UI details remain provisional.

### 2. Negotiable agreement terms — latest scope proposal

The user has currently limited the fields that either party can alter during structured negotiation to:
- **Booking dates** — the date attached to each booking-period entry.
- **Booking times** — the From/To period and the corresponding generated slots for each booking-period entry.
- **Bid price** — the customer's offered amount for each booking-period entry.

The price can be altered per booking period. Date/time changes must be validated against current available inventory. A proposed change cannot include disabled, already reserved, conflicting or otherwise unavailable slots.

The customer's intended-use information and answers to provider-defined questions are supporting information supplied with the proposal. They are not currently listed as negotiable fields. The owner reviews them when deciding whether to accept, alter the booking dates/times/prices, or reject the entire proposal. If the intended use or supporting information is unacceptable, the provider may reject the proposal in its entirety; there is no separate reject action against an individual informational answer in the current scope.

Additional event spaces, facilities and services have since been revived as optional proposal components. They are included in the current working negotiation model as separate blocks; this supersedes the earlier shelved/deferred note above. Their detailed creation, pricing and selection rules remain provisional.

### 3. Negotiation and proposal progression — latest working model

The proposal is negotiated through successive complete versions. The parties can accept all current terms, alter the permitted fields (dates, times or prices), or end the proposal in its entirety. There is no individual-field Reject action: when a party does not accept a particular date, time or price, they propose an alternative value instead.

#### A. Customer submits the initial bid
1. The customer selects one or more booking-period entries. Each entry contains one date, one continuous From/To period selected from generated available slots, and a bid price for that period.
2. The customer supplies the intended-use information and answers the provider's configured Open/Fixed questions. Fixed questions may be set to single-select or multi-select.
3. The customer submits the complete proposal. It is sent to the provider for review. Submission does not reserve the slots.

#### B. Provider reviews the bid
For each negotiable booking term, the provider can:
- **Accept** the proposed date/time/price value as submitted; or
- **Alter** it by proposing a replacement date, time or price.

The provider does not reject an individual term. If the provider cannot agree to the proposal, they can **Reject bid**, which rejects the entire proposal.

After reviewing all terms:
- If the provider accepts every term without making alterations, they can press **Bid accepted** and proceed to the final confirmation invitation described below.
- If the provider changes any date, time or price, they press **Send alteration**. The revised complete proposal is sent to the customer. No payment gate or agreement invitation is opened at this stage.

#### C. Customer reviews the provider's alterations
The customer reviews the complete revised proposal and can:
- **Accept all alterations** — the customer accepts the complete current proposal version. The accepted version is returned to the provider. The provider then chooses whether to initiate the applicable final confirmation step (payment gate or two-way agreement invitation). The customer accepting the counterproposal does not itself open the payment gate or reserve the slots.
- **Send alterations** — the customer changes one or more permitted fields (dates, times and/or bid prices) and sends a new complete counterproposal to the provider. No payment gate or agreement invitation is opened.
- **Cancel bid** — the customer withdraws the entire proposal.

The provider and customer continue this process until one party accepts all the current terms without making further alterations, or the proposal is rejected/withdrawn.

#### D. Complete-version and history rules
1. Every initial bid or counteroffer is stored as a complete proposal version containing all booking periods, dates, times, bid prices and the supporting customer information associated with the proposal.
2. Each alteration creates a new version. The application retains earlier versions and identifies who made each change.
3. The receiving party reviews the latest complete version. Accepting means accepting all its current negotiable terms together; it is not a collection of partial acceptances from different versions.
4. If either party changes any permitted term, the changed proposal becomes a new version and must be reviewed by the other party. Earlier acceptance does not carry over to the altered version.
5. The only structured fields that can currently be altered during negotiation are booking dates, booking times and bid prices. The supporting use-description and answers to provider-defined questions remain attached as information, not as editable negotiation terms.
6. A date/time alteration must be checked against current inventory. The proposal cannot proceed with slots that are no longer available. If part of a multi-period proposal cannot be accommodated, the party must revise the affected booking period(s) or reject/cancel the proposal as a whole; the system must not silently remove requested periods.

#### E. Final confirmation after the terms are accepted
Once the provider has accepted all current terms without making an alteration, the provider explicitly initiates the relevant final confirmation action:
- **Platform payments enabled:** the provider opens the payment gate for the selected customer. The customer receives a notification and can complete payment or cancel/withdraw. The payment gate has a timer. The slot is not confirmed/reserved until payment succeeds.
- **Platform payments disabled:** the provider sends a two-way agreement invitation to the customer. The customer receives a notification and can press **Agree** or cancel/withdraw. The invitation has a timer. The booking is confirmed/reserved when the customer agrees to the complete terms.

The provider does not open a payment gate or agreement invitation while sending alterations. The button/action for a response that changes any terms is **Send alteration**, not **Bid accepted**, **Open payment gate** or **Send agreement invitation**.

The precise timer duration, expiry outcome and whether the provider can revoke an invitation before expiry remain open for later refinement. The existing rule that only one active payment gate may exist for the same or overlapping bookable inventory continues to apply.

#### F. Whole-proposal outcomes
- **Provider Reject bid:** rejects the complete proposal, not just one field.
- **Customer Cancel bid:** withdraws the complete proposal, not just one booking period.
- **Provider Send alteration / Customer Send alterations:** creates a new complete version; negotiation continues.
- **Provider opens payment gate / sends agreement invitation:** initiates final customer action only after the provider has accepted all current terms without alterations.
- **Payment succeeds / customer agrees:** confirms and reserves the relevant inventory.

### 4. Suggested proposal page layout

- **Booking periods:** each requested date and From/To range, the selected slot count, starting-price subtotal and customer's bid price for that period.
- **Intended use:** activity/event name, type and description.
- **Customer details requested by provider:** Open/Fixed question-and-answer pairs, including whether each Fixed answer was single-select or multi-select.
- **Negotiation terms:** dates, times and bid prices, with clear Accept or Alter controls for each permitted term.
- **Negotiation history:** chronological complete proposal versions, author, timestamp and highlighted changes.
- **Whole-proposal actions:** Bid accepted (provider, when all terms are accepted without alterations), Send alteration (either party when proposing changes), Reject bid (provider, whole proposal), and Cancel bid (customer, whole proposal).
- **Final confirmation:** after the provider accepts the complete current terms, the provider can open the payment gate or send the two-way agreement invitation according to the event space's payment setting.

Keep the proposal overview readable on Android. Detailed versions and individual fields can be opened within the proposal page; do not put all questions and negotiation history into the slot list.

### 5. Open questions

- Should the provider-defined question builder be configured per event space (recommended for MVP), or should it also support date-specific questions?
- The precise Android control and presentation for the Fixed-field single-select/multi-select setting can be finalised during UI design.
- The Required/Optional control and question removal are included in the proposed builder. Should providers be allowed to edit an existing question after bids have been submitted? Historic proposals should preserve the exact question and answer snapshot.
- Which standard information fields are mandatory: activity name/type, description, attendee count, setup requirements and equipment needs?
- Should customer answers to provider-defined questions be purely informational, or can the provider mark selected questions as agreement terms? The recommendation is to keep information and binding terms distinct, with explicit agreement terms created where necessary.
- Should either party be able to request clarification without formally counteroffering, or should every response be either accept, alter/counteroffer or reject/withdraw the whole proposal?
- What are the exact timer durations and expiry outcomes for the payment gate and the no-payment agreement invitation?
- Does the previously discussed two-minute below-minimum auto-rejection apply before provider-defined questions and negotiation, or should it remain an automatic rejection of the submitted booking-period offer regardless of the extra information? This remains open for confirmation.

## Change log (continued)
- Added a provisional design for provider-defined customer questions and a structured negotiation model. The proposal separates informational answers from mutually agreed booking terms, recommends a constrained per-event-space question builder, and uses complete proposal versions with field-level editing and whole-version acceptance. Nothing in this addition is locked.

- Refined the provisional provider-defined question builder to use two tabs: **Open field** (question plus Short text, Long text or Number response type) and **Fixed field** (question plus provider-defined answer options). Added Required/Optional controls, an Add action, a list of created questions and removal. Fixed-field single-select versus multi-select is now specified as a provider choice. The earlier note shelving additional event spaces/facilities/services was superseded when the user revived and developed optional add-ons; see the current working proposal under section C.


- Updated the Fixed-field question builder: providers can now specify whether customers may select one predefined answer or multiple answers.
- Added the user's latest negotiation direction as a working proposal: provider may accept or alter individual date/time/price terms, but cannot reject an individual term; rejecting means rejecting the whole bid. Customer may accept all alterations, send their own alterations, or cancel the entire bid. Any alteration creates a new complete proposal version and does not open a payment gate or agreement invitation. Only after the provider accepts all current terms without altering them does the provider initiate the payment gate (payments enabled) or two-way agreement invitation (payments disabled). Both final invitations have a timer; exact duration and expiry behaviour remain open. The only currently alterable negotiation fields are booking dates, times and bid prices; intended-use details and custom question answers remain supporting information. This workflow is provisional and not locked.

### Negotiation clarification — staged acceptance, alteration and removal (provisional)

This is the latest clarification to the negotiation model. It refines the earlier proposal-versioning description and supersedes any conflicting assumption that accepting/rejecting an individual change immediately mutates the current proposal.

#### Core rule: decisions are staged until the response is submitted
- The currently active proposal remains unchanged while either party reviews and responds to the other party's proposed changes.
- Accepting or rejecting a proposed alteration/removal records a response in the recipient's draft review. It does not immediately change or remove the block/field from the active proposal.
- Only when the recipient completes and sends the final review is a new proposal version created with the resolved outcomes applied.

#### Proposed removal of a block
1. One party proposes rejecting/removing an entire block (for example, one booking period or one optional item).
2. The block remains visible in the active proposal while the other party reviews the removal request.
3. If the other party **accepts the rejection/removal**, the block is marked for removal in the draft response. It is actually omitted only when that party sends the final review/new proposal version.
4. If the other party **rejects the rejection/removal request**, the block remains in the proposal with its existing values in the next version. It is not removed.

#### Proposed alteration of a field
1. One party proposes an altered value for a field (for example, a different date, time or price).
2. The existing value remains the active value while the other party reviews the proposed alteration.
3. If the other party **accepts the alteration**, the new value is staged in the draft response and becomes the active value only when the final review/new proposal version is sent.
4. If the other party **rejects the alteration**, the previous value remains unchanged in the next version. The rejected replacement value is not applied.

#### Final review and version creation
- The response screen must show all proposed alterations and proposed removals, alongside the recipient's decisions on them.
- The recipient can review the complete response before sending it.
- On **Send final review** (wording to be finalised), the system creates the next complete proposal version: accepted alterations are applied, accepted removals are omitted, rejected alterations retain their previous values, and rejected removal requests retain their blocks.
- Until that send action occurs, the current proposal version remains intact. The system must retain the full history of the request, response and resulting version.

#### Example
If a provider proposes removing Booking Period 2, the customer can accept the removal or reject the removal request. Accepting means Period 2 is removed from the next version once the customer sends the final review; rejecting means Period 2 remains unchanged. Similarly, if the provider proposes changing a price from ₹2,500 to ₹3,000, accepting applies ₹3,000 in the next version after submission; rejecting keeps ₹2,500.

This staged-response model is provisional and is not locked. It should be treated as the current working interpretation where earlier negotiation notes describe immediate acceptance/removal or do not distinguish proposed changes from committed changes.

## Change log (continued)
- Clarified negotiation response semantics: accept/reject decisions on proposed alterations and proposed block removals are staged in a draft review. They do not immediately modify the current proposal. The changes are applied only when the recipient sends the final review/new proposal version. Accepting a removal removes the block in that submitted version; rejecting the removal keeps the block. Accepting an alteration applies the replacement value in that submitted version; rejecting the alteration preserves the previous value. This clarification is provisional and not locked.


## Negotiation history and final review — working proposal

> This section records the user's latest proposal and design assessment only. It is **provisional**, not locked. It supplements the existing staged proposal-version and accept/reject/alter rules above.

### 1. History visibility

The normal proposal view should prioritise the **current effective value and its current negotiation status**. It should not display the entire historical sequence beside every field.

- Each negotiable block (for example, one booking period, one additional event space, one facility or one service) should expose a **View history** action.
- Selecting it opens a dedicated history view for that block. On Android, use a full-screen page or appropriately sized sheet for longer histories; avoid a small pop-up that becomes tedious to scroll or read.
- The block history shows the sequence of submitted changes, including version, actor (customer/provider), timestamp, action, previous value, proposed value and the outcome (accepted, rejected, applied or not applied).
- For a booking-period block, the history can show field-level changes to date, start time, end time and price within that block. For an optional addition, it can show inclusion/removal decisions and price changes.
- A proposal-level history/timeline may provide a chronological overview of all submitted versions, with links into the relevant block histories. This is preferable to forcing users to inspect every block to understand the overall progression.
- Submitted versions and decisions must be immutable. A rejected or removed block disappears from the current active proposal only when the relevant final review is sent, but remains accessible in history as a removed item. A rejected alteration remains in history but does not replace the current value.
- Unsubmitted draft interactions may be shown as a local draft state, but should not be represented as a committed proposal version. A version is created when the response/final review is submitted.

### 2. Current proposal display

- The current proposal shows the latest effective values and a concise status for each block/field.
- Historical values are opened through the block's History action rather than always shown inline.
- A changed value should make clear whether it is merely proposed, awaiting the other party's decision, or already applied in a submitted version.
- A block proposed for removal remains in the current proposal until the other party accepts the removal and submits the final review. After that, it is absent from the active proposal but remains in history.

### 3. Final review and acknowledgement

The final review is an **accuracy check of the complete, agreed proposal**, not a second negotiation round and not, by itself, the payment or no-payment reservation action.

- Show the complete current proposal, including the primary booking periods, any selected additional event spaces, facilities and services, supporting customer information, prices and applicable booking terms.
- The purpose is to let the reviewing party verify that the information and agreed values are correct before using the final confirmation mechanism.
- If something is incorrect, the party must return to negotiation rather than silently editing the final-review screen. Any change must go through the existing proposal-versioning process.
- The proposal must be in a fully agreed state before the final confirmation gate is made available. The final review must refer to one complete, identifiable proposal version.
- Keep the final review read-only apart from navigation back to negotiation and the relevant final confirmation action.

### 4. Final confirmation gate: payment OR agreement

Once the relevant terms have been fully reviewed and accepted, the final confirmation path is conditional on the event-space payment setting:
- **Payments enabled:** the provider opens the payment gate for the selected customer. The customer receives the payment invitation and must pay within the configured timer. Successful payment confirms and reserves the booking.
- **Payments disabled:** the provider sends the no-payment agreement/reservation invitation. The customer receives it and presses **Agree** within the configured timer. Completion of the required two-way agreement confirms and reserves the booking.
- The two gates are alternatives, not sequential steps: a proposal uses the payment gate **or** the no-payment agreement gate, never both.
- Existing exclusivity remains: only one active payment/agreement hold should control the same or overlapping inventory at a time. Other bids requiring that inventory cannot be confirmed while the hold is active.

The final review and the final confirmation gate have distinct purposes: final review checks that the agreed information is correct; the payment gate or no-payment reservation/agreement gate is the action that completes the booking. They are not duplicate acceptance steps.

### 5. Booking record and post-confirmation actions

- When payment succeeds or the no-payment agreement is completed, save the accepted proposal version as the confirmed booking/agreement snapshot.
- The booking record should retain the final agreed dates, times, primary space, selected optional additions, prices, applicable policies, and relevant customer/provider information.
- The booking remains accessible from a dedicated reservation/booking area so either party can inspect the agreed terms and history.
- Post-confirmation actions such as customer cancellation, provider cancellation, refund requests and disputes should be handled through booking-management workflows, not by editing the historical negotiation proposal.
- Any amendment after confirmation should create a separate amendment/cancellation process and must not rewrite the accepted booking snapshot.

### 6. Critical design assessment

- Per-block history is a good fit for Android because it keeps the main proposal readable while preserving traceability. A proposal-level timeline is also useful for seeing the overall sequence without opening every block.
- Store complete immutable proposal versions plus structured change events. The block-level history is a filtered view of that shared record, not a separate mutable history store.
- Record who made each change, when, the affected block/field, old and new values, decision, submission/version identifier and whether the change was applied.
- Do not delete rejected or removed content from the audit history. Remove it only from the active proposal once the final review is submitted.
- Avoid forcing users to press Accept on every unchanged field. Use block/section-level acceptance and one overall review confirmation, while preserving field-level controls for actual changes.
- Keep negotiation approval, final payment/agreement confirmation and post-booking cancellation/refund as distinct lifecycle stages.
- The payment gate and no-payment agreement gate are mutually exclusive. Their timer, expiry and slot-release behaviour remain open.

### 7. Open decisions

- Which party sees the final review first, and does the provider explicitly open/send the payment or no-payment reservation gate from that review, as previously proposed?
- Should final review display a single overall confirmation/continue action, or should it also provide section-level completion indicators? It should not require re-acceptance of each unchanged field.
- Should the proposal-level history timeline be included in the first release alongside per-block history, or start with per-block history only?
- What exact labels/statuses should distinguish draft changes, submitted alterations, accepted alterations, rejected alterations, proposed removals and completed removals?
- What are the exact timer durations and outcomes for payment and no-payment agreement gates?
- What cancellation, amendment and refund actions are available to each party after a booking has been confirmed?


- Added a provisional negotiation-history and final-review design: current values/statuses in the main proposal, per-block history drill-down, a proposal-level timeline option, immutable submitted versions/change records, staged decisions applied only on final-review submission, section-level acceptance plus an overall review action, and mutually exclusive payment versus no-payment agreement gates. Flagged possible duplicate acceptance between final review and the final no-payment agreement gate. No new decisions were locked.

### User feedback — negotiation history and final review assessment
- The user agreed with the preceding critical review of the negotiation-history, final-review, payment/agreement gate and confirmed-booking design.
- Record this as an **approved working direction**, but do not move it into the Locked decisions section: the user has established that decisions are locked only when they explicitly instruct us to lock them in.
- The detailed open decisions listed above remain unresolved, especially whether final review is only a completeness/accuracy check or itself constitutes binding mutual acceptance before the payment/agreement gate.

### Final review and post-confirmation display — user clarification (provisional)

The user clarified the intended distinction between final review, final confirmation and the post-confirmation booking view:

1. **Final review purpose:** The final review is to check that all mutually agreed booking information is correct. It is not another negotiation round. If a value or component is wrong, the party returns to negotiation; changes are made through a new proposal version, not by editing the read-only review itself.
2. **Final confirmation:** Once the information has been checked, the customer uses the relevant gate that the provider has opened/sent:
   - With platform payments enabled, the customer uses the **payment gate** and completes payment.
   - With platform payments disabled, the customer uses the **reservation/agreement gate** and presses **Agree** to complete the no-payment reservation.
   - These remain alternative paths; a booking uses one gate or the other, never both.
3. **After successful confirmation:** The bid/booking is displayed in **read-only mode**. The accepted proposal details are preserved and cannot be edited through the negotiation screen. The record can still expose the permitted post-booking actions, such as cancelling the reservation or requesting/receiving a refund according to the applicable policy.
4. **Post-confirmation status:** A successfully paid booking is identified as **Paid** (or the equivalent paid-booking status); a completed no-payment agreement is identified as **Reserved**. The underlying negotiated terms remain the same read-only agreement snapshot.
5. **Cancellation/refund timing:** The available cancellation/refund action must reflect the applicable refund cutoff and the already locked cancellation policy. The existing locked rule says that a voluntary customer cancellation after the refund cutoff returns 50% of the booking payment less applicable holding costs, with the other 50% going to the provider. The precise wording and controls shown before versus after the cutoff still need final UI definition; do not silently reinterpret the locked post-cutoff refund amount as zero.
6. **History:** Negotiation history remains accessible from the confirmed booking, but history is read-only. A cancellation or refund is a separate post-booking transaction and must not rewrite the accepted proposal version.

This clarification resolves the previous ambiguity about whether final review is itself the binding commitment: final review checks correctness; successful payment or completion of the reservation/agreement gate confirms the booking. The detailed gate ownership, exact button wording, and before/after-cutoff cancellation presentation remain open. This clarification is provisional and has not been moved to Locked decisions because the user has not explicitly asked to lock it in.

## Change log (continued)
- Clarified final review as an accuracy check of the mutually agreed proposal, distinct from the final payment/reservation action. After successful payment or agreement, the bid becomes a read-only confirmed booking with applicable cancellation/refund actions; paid and no-payment bookings are distinguished as Paid and Reserved. Added the user's clarification as provisional and removed the now-resolved ambiguity about whether final review duplicates the agreement gate.

- Locked the high-level confirmed-booking management structure after the user explicitly approved it: dedicated Bookings area for both parties; read-only confirmed booking/agreement snapshot; access to agreement and immutable negotiation history; payment/payout visibility for paid bookings; separate cancellation and refund workflows; whole-booking cancellation only in the initial scope (partial cancellation of periods/additions deferred); Paid versus Reserved status; and staged provider payout visibility. Exact UI, pre-cutoff voluntary cancellation refund amount, holding-cost calculation, provider-cancellation/dispute details and payment-provider feasibility remain open.


- Added the user's latest Role Management proposal as a provisional permission model: Basic mode grants all normal actions within an enabled section; Advanced mode grants only specifically selected actions. Added draft permission sections derived from event-space management, date/schedule management, proposals/negotiations, additional spaces/facilities/services, confirmed bookings, payments/refunds/payouts, and user/role management. Recorded recommendations to separate resource scope from action permissions and to isolate sensitive financial and administrative powers. Nothing in this role model is locked.


- **Locked the Event Spaces permission set in Role Management** after the user explicitly approved it. Core permissions: View event spaces, Create event space (complete standard creation workflow), Edit event space (standard/core information and configuration), and Disable event space (remove customer-marketplace visibility rather than delete). Optional permissions: Manage customer questions, Manage additional event spaces, Manage facilities and Manage services. Optional configuration permissions are not prerequisites for standard Create/Edit. Disabling preserves provider access and records/history, allows later re-enabling, and does not cancel existing confirmed bookings. The treatment of already-pending bids and active payment/agreement holds when disabling remains open.
- Revised the provisional Role Management permission proposal after the user's clarification: Date Management should have one consolidated View permission for all date information/configuration; Manage day configuration should be decomposed into field-operation permissions (date active/inactive, operating windows, slot amount/type, starting price per slot, minimum acceptable bid per slot, and an unresolved reset/default-restoration permission); Manage individual time slots remains one complete permission covering ON/OFF actions and all safeguards/consequences. Added Bid Management as a separate top-level permission section. These are provisional and not locked.

- Recorded the Date Management permission proposal as an **approved working direction, not locked**: one consolidated View Date Management permission; granular Manage Day Configuration permissions (date active/inactive, operating windows, slot amount/type, starting price per slot, minimum acceptable bid per slot, and proposed reset permission); and one complete Manage Individual Time Slots permission covering ON/OFF actions and their safeguards/consequences. Bid Management remains a separate top-level permission section with child permissions still to be designed.

---

## Bid Management permissions — locked decision detail

> This is the locked permission structure for the Role Management section **Bid Management**. Apply the user's permission-design rule: grant complete operations, including all actions mandatory to finish that operation; do not split a complete operation into arbitrary optional field/action permissions.

### 1. View Bid Management — one consolidated permission

Grants read-only access to the complete bid-management experience for the event spaces within the person's assigned access scope:
- View incoming bids and their current statuses.
- View the complete submitted proposal: primary booking periods, requested additional event spaces, selected facilities and services, per-period and per-addition offers, intended-use information, and answers to provider-configured customer questions.
- View all relevant block-level and field-level statuses (for example, accepted, altered, proposed for removal, pending response, and resolved).
- View the current effective proposal and its submitted-version history, including the overall proposal timeline and individual block histories.
- View bid counts and relevant customer/proposal information made available at this stage of the workflow.

This permission is read-only. It does not permit accepting, rejecting, altering, submitting a counterproposal, opening a payment/agreement gate, or otherwise changing a bid. It does not automatically grant access to private customer contact details that are only shared at a later agreed stage.

### 2. Manage Bids — one complete operation

Grants the person the ability to carry out the complete provider-side bid-handling workflow for the event spaces within their assigned access scope. It includes the viewing access needed to perform the work; a separate View permission is not required to make this operation usable.

The complete operation includes:
- Open and review the full proposal, including customer-provided information and answers to custom questions.
- Review the primary booking-period blocks and optional additional-space, facility and service blocks.
- Accept or reject a complete block.
- Accept or alter an individual negotiable field within a block.
- Enter and save replacement values for fields marked for alteration.
- Review and respond to the other party's proposed alterations and proposed removals; accept or reject those proposed changes.
- Apply staged decisions only when the response/final review is submitted. Do not mutate the active proposal immediately when a decision is merely selected in the draft interface.
- Reject the entire bid.
- Prepare and submit the provider's counterproposal/final review, and process subsequent customer responses through the same negotiation lifecycle.
- Where all required terms have been agreed and the final review is correct, initiate the appropriate final confirmation path: open the payment gate when platform payments are enabled, or send/open the no-payment reservation/agreement gate when payments are disabled. The existing configured gate timer and inventory-exclusivity rules apply.
- View the resulting status and hand-off to the confirmed-booking workflow after successful payment or agreement.

This permission does **not** grant unrelated management powers:
- It does not allow changing the event-space listing, custom-question definitions, general availability rules, date-level configurations, slot duration, starting price or minimum acceptable bid. Those belong to Event Spaces or Date Management permissions.
- It does not allow switching individual generated slots ON/OFF. That belongs to Date Management.
- It does not allow creating/editing/deleting the catalogue definitions of facilities or services. Those belong to Event Spaces/catalogue permissions.
- It does not allow editing payment/refund policies, processing refunds, managing payouts or changing payment-provider configuration. Those belong to Payments and Refunds.
- It does not allow cancelling or amending an already confirmed booking. Those belong to Confirmed Bookings.

### 3. Rules that apply regardless of Bid Management permission

- The configured minimum acceptable bid remains private. A bid below the applicable minimum is automatically rejected after the agreed two-minute delay; the user is notified that the bid has been rejected. A role permission must not bypass this rule.
- Each primary booking period and each optional addition is handled as its own proposal block. A rejected block is removed from the active proposal only after the other party accepts the proposed removal and submits the final review. If the proposed removal is rejected, the original block remains.
- If every primary booking period is rejected, the complete bid is rejected; optional additions cannot keep a bid alive without a primary booking period.
- Additional event spaces retain their own availability and pricing. Selecting one through the primary event-space listing does not bypass its schedule or permit a conflicting reservation.
- Submitted proposal versions and history remain immutable. A confirmed booking is handed over to Confirmed Bookings and is no longer edited through Bid Management.

### 4. Basic/Advanced presentation

- In Basic mode, enabling the Bid Management section grants both **View Bid Management** and **Manage Bids** (full ordinary bid-handling access).
- In Advanced mode, the provider may grant **View Bid Management** alone for read-only access, or **Manage Bids** for the complete workflow. Manage Bids includes the viewing capability it requires; the user must not be left with management actions but without the information needed to perform them.
- Do not create separate permissions for accepting fields, rejecting blocks, altering prices, reviewing history, submitting counterproposals or opening the appropriate gate. These are constituent actions of the complete Manage Bids operation, not independent optional capabilities under the user's current permission principle.

### 5. Scope and unresolved details

- The role's event-space/resource scope (which event spaces the person can work on) is separate from the action permissions above. The assignment model still needs to be settled.
- Whether Bid Management should expose all customer identity/contact information or only the information needed to evaluate the proposal remains subject to the platform's customer-contact-sharing rules. This proposal does not grant early access to contact details that are otherwise withheld.
- Exact gate timers, expiry handling and the payment-provider implementation remain governed by the booking/payment design; this permission only authorises initiating the correct configured gate.

---

### Latest Role Management status

- **Event Spaces:** locked permission set (View, Create, Edit, Disable; plus separately controlled optional permissions for customer questions, linked additional spaces, facilities and services).
- **Date Management:** approved working permission design, not locked (one consolidated View permission; granular Manage Day Configuration operations; one complete Manage Individual Time Slots permission).
- **Bid Management:** locked permission set under Locked decisions → H: one consolidated read-only View permission and one complete Manage Bids permission that includes all actions required to complete provider-side bid handling through opening the correct final confirmation gate.
- **Confirmed Bookings and Payments/Refunds:** their child permissions remain to be designed. **Role Management administration:** the Roles and People permission options are now locked under Locked decisions → I; the exact event-space scope/assignment model remains open.

### Role Management administration permissions — locked status

- **Role Management administration permissions are locked** under Locked decisions → I. Roles: View, Create, Edit and Delete. People: View, Add/Invite, Change role/access assignment and Remove access.
- Adding a person requires an initial access assignment. Existing roles can be assigned; creating a custom role inline requires Create role permission. Inline-created custom roles are saved as reusable named roles.
- Editing a reusable role shows affected people before the change is applied. Assigned roles must be resolved before a role is deleted. Historical actions and permission snapshots are preserved.
- Role permissions and the event-space scope of each person's assignment remain separate. The exact scope/assignment model is still open.

### Change log — Bid Management permissions

- Added a proposed Bid Management permission structure with one consolidated read-only View permission and one complete Manage Bids permission. After the user explicitly approved it, moved the structure into Locked decisions → H. Manage Bids includes the mandatory provider-side proposal review, block/field decisions, alteration and removal handling, counterproposal submission, whole-bid rejection, final review submission and initiation of the appropriate payment or no-payment agreement gate. It excludes event-space/date configuration, slot ON/OFF, catalogue administration, refund/payout administration and post-confirmation booking cancellation.

- **Locked Role Management administration permissions** after the user explicitly approved them: Roles (View, Create, Edit, Delete) and People (View, Add/Invite, Change role/access assignment, Remove access), including complete-operation requirements, Basic/Advanced configuration, inline custom-role creation dependency and safeguards. Inline-created custom roles are reusable named roles. The exact event-space scope/assignment model remains open.

- **Locked Role Management administration permissions** on 11 November 2026 after the user explicitly approved the Roles and People structure: View/Create/Edit/Delete roles and View/Add-or-invite/Change-assignment/Remove-access people operations. Basic mode grants ordinary operations across both areas; Advanced mode exposes the eight individual operations. Inline custom roles require Create role permission and are saved as reusable named roles. Deletion/revocation preserves historical records and permission snapshots; role deletion requires existing assignments to be resolved. The exact per-person event-space scope model remains open.

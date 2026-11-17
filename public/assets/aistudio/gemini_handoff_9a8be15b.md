# Gemini Handoff — Venue Management & Booking Platform

Use this note together with `event_space_product_spec.md`, which is the detailed living product specification and decision record.

## Product direction

Build an Android application for owners/operators of rentable spaces. The primary product is venue operations and management: spaces, availability, schedules, bids, reservations and team access. A secondary marketplace lets customers discover spaces and submit booking proposals. Public event discovery/advertising is out of scope for the current product.

An account can use both provider and customer functionality. A provider can manage multiple event spaces.

## Critical instruction: respect decision status

The master specification separates **Locked decisions** from **Open / provisional details**. Treat only items explicitly in the Locked decisions section as fixed requirements. Do not silently convert proposals, recommendations or unresolved details into requirements. When an open point blocks implementation, identify it and ask a focused question; otherwise use a clearly labelled temporary implementation assumption.

The user prefers to design and lock one area at a time. Avoid redesigning locked decisions or expanding scope without asking.

## Locked areas (high level)

1. **Booking confirmation paths:** payment-enabled bookings use a provider-opened payment gate for one selected customer per conflicting/overlapping inventory; payment-disabled bookings use a two-way agreement gate. These are alternatives.
2. **Refund/payout principles:** post-cutoff voluntary customer cancellation returns 50% less applicable holding costs; qualifying provider-caused unusability can result in a full refund when at least 20% of booked time is lost and the customer does not use the space; three-day evidence window; proposed 50%/50% staged payout with provider availability confirmation at cutoff. Payment-provider/legal feasibility and several calculation details remain open.
3. **Date navigation:** next seven days as horizontally scrollable daily cards; From/To date search with quick ranges; longer results grouped by month; selecting a date opens Day configuration and Time slots.
4. **Confirmed-booking management:** dedicated Bookings area for both parties; confirmed agreement snapshot is read-only; history remains accessible; cancellation/refund activity is recorded separately; initial cancellation applies to the whole booking, not individual periods/additions; statuses distinguish Paid and Reserved.
5. **Event Spaces permissions:** View, Create (complete standard creation workflow), Edit, Disable (not Delete); separate optional permissions for customer questions, linked additional event spaces, facilities and services.
6. **Bid Management permissions:** one consolidated View permission and one complete Manage Bids permission, including the required provider-side proposal/negotiation and final-gate workflow.
7. **Role Management administration permissions:** Roles area (View, Create, Edit, Delete) and People area (View, Add/Invite, Change assignment, Remove access). Basic mode grants ordinary operations across both areas; Advanced exposes individual operations. Inline custom roles require Create role permission and are saved as reusable named roles. Preserve audit history and prevent privilege escalation. The exact event-space scope/assignment model remains open.

## Major open/provisional areas to resolve or implement carefully

- Detailed event-space creation fields and validation. The current field groups are documented in the master specification; the user may refine the creation UI in AI Studio.
- Detailed Date Management behaviour and its permission structure. Its permissions are an approved working proposal but are not formally locked. Individual generated slots have ON/OFF controls; disabling a slot requires warnings and can reject affected pending bids or cancel eligible bookings subject to refund-cutoff rules.
- Customer bid submission details, including multiple booking periods, customer questionnaire, optional additional spaces/facilities/services, pricing and validation.
- Proposal negotiation UI and exact status transitions. The current proposed model uses block-level Accept/Reject and field-level Accept/Alter; decisions are staged and only applied when a response/final review is submitted. Rejected removals/alterations leave the original content intact. Verify details against the master specification.
- Proposal history UI: immutable submitted versions, per-block history and an overall timeline are proposed/approved in discussion; exact first-release scope and labels remain to be finalized.
- Final review checks correctness; payment or agreement gate is the actual booking confirmation. Exact gate timers and expiry handling remain open.
- Facilities/services catalogue and their time-rate vs fixed-rate pricing details; additional event-space linking and cross-space inventory conflicts.
- Exact per-person event-space scope in Role Management (e.g. one role across all assigned spaces versus different roles/scopes by space).
- Confirmed Bookings and Payments/Refunds child permissions are not yet defined.
- Refund cutoff X, holding-cost calculation, pre-cutoff cancellation handling, evidence adjudication, provider response/dispute process, and payment provider/jurisdiction feasibility.

## Recommended starting approach in Gemini

1. Read `event_space_product_spec.md` in full before proposing architecture or code.
2. Produce a concise implementation map separating locked requirements, provisional proposals and open questions.
3. Identify the minimum data model needed for accounts, event spaces, availability rules/exceptions, generated slots, proposals/versions/blocks, payment or agreement gates, confirmed bookings, catalogue additions, roles and assignments.
4. Preserve audit/version history. Do not overwrite submitted proposal versions or accepted booking snapshots.
5. Plan the Android UI mobile-first. For narrow screens, adapt layouts without changing the agreed interaction model.
6. Implement in small vertical slices, beginning with the foundational data model and the user's selected priority. Do not attempt the entire application in one generated pass.


package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Data class representing an operating time window for recurring availability.
 * For example, 08:00 to 18:00 on ["MON", "TUE", "WED", "THU", "FRI"].
 */
data class OperatingWindow(
    val id: String = UUID.randomUUID().toString(),
    val startTime: String = "09:00",
    val endTime: String = "17:00",
    val daysOfWeek: List<String> = listOf("MON", "TUE", "WED", "THU", "FRI")
)

/**
 * Room Entity representing a physical rentable event space.
 * Persists persistent configuration, tags, operating rules, slot sizing, and payment preferences.
 */
@Entity(tableName = "event_spaces")
data class EventSpaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val tags: List<String> = emptyList(),
    val description: String = "",
    val spaceType: String = "Indoor", // "Indoor", "Outdoor", "Hybrid"
    val capacity: Int = 50,
    val customSpecifications: Map<String, String> = emptyMap(),
    val address: String = "",
    val operatingWindowsJson: String = "", // JSON list of OperatingWindow
    val slotDurationAmount: Int = 60,
    val slotDurationUnit: String = "MINUTES", // "MINUTES" or "HOURS"
    val startingPricePerSlot: Double = 50.0,
    val currency: String = "USD",
    val unavailableDatesJson: String = "[]", // JSON list of string dates
    val isBiddingEnabled: Boolean = true,
    val minAcceptableBidPerSlot: Double? = 40.0,
    val isPlatformPaymentsEnabled: Boolean = true,
    val refundCutoffDays: Int = 7,
    val paymentGateTimerMinutes: Int = 30,
    val additionalInfo: String = "",
    val coverImageUri: String? = null,
    val isDisabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

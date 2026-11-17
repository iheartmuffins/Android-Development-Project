package com.example.data.repository

import com.example.data.local.EventSpaceDao
import com.example.data.model.EventSpaceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class EventSpaceRepository(private val dao: EventSpaceDao) {

    val allSpaces: Flow<List<EventSpaceEntity>> = dao.getAllSpaces()

    fun getSpaceById(id: Long): Flow<EventSpaceEntity?> = dao.getSpaceById(id)

    suspend fun getSpaceByIdOnce(id: Long): EventSpaceEntity? = withContext(Dispatchers.IO) {
        dao.getSpaceByIdOnce(id)
    }

    suspend fun insertSpace(space: EventSpaceEntity): Long = withContext(Dispatchers.IO) {
        dao.insertSpace(space)
    }

    suspend fun updateSpace(space: EventSpaceEntity) = withContext(Dispatchers.IO) {
        dao.updateSpace(space)
    }

    suspend fun deleteSpace(space: EventSpaceEntity) = withContext(Dispatchers.IO) {
        dao.deleteSpace(space)
    }

    suspend fun toggleDisabled(id: Long, isDisabled: Boolean) = withContext(Dispatchers.IO) {
        dao.updateDisabledStatus(id, isDisabled)
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        if (dao.countSpaces() == 0) {
            val sampleBallroomWindows = JSONArray().apply {
                put(JSONObject().apply {
                    put("id", "w1")
                    put("startTime", "08:00")
                    put("endTime", "23:00")
                    put("daysOfWeek", JSONArray(listOf("FRI", "SAT", "SUN")))
                })
                put(JSONObject().apply {
                    put("id", "w2")
                    put("startTime", "09:00")
                    put("endTime", "18:00")
                    put("daysOfWeek", JSONArray(listOf("MON", "TUE", "WED", "THU")))
                })
            }.toString()

            val sampleStudioWindows = JSONArray().apply {
                put(JSONObject().apply {
                    put("id", "w3")
                    put("startTime", "08:00")
                    put("endTime", "20:00")
                    put("daysOfWeek", JSONArray(listOf("MON", "TUE", "WED", "THU", "FRI", "SAT")))
                })
            }.toString()

            val ballroom = EventSpaceEntity(
                name = "Grand Oak Ballroom",
                tags = listOf("Weddings", "Galas", "Conferences", "Banquets"),
                description = "Opulent chandelier-lit ballroom with high ceilings, parquet hardwood dance floor, and a dedicated bridal suite.",
                spaceType = "Indoor",
                capacity = 250,
                customSpecifications = mapOf(
                    "Restrooms" to "4 Private Accessible Restrooms",
                    "Audio/Visual" to "Dual 4K Laser Projectors & Wireless Shure Microphones",
                    "Kitchen" to "Commercial Catering Preparation Station"
                ),
                address = "1400 Grand Boulevard, Financial District",
                operatingWindowsJson = sampleBallroomWindows,
                slotDurationAmount = 60,
                slotDurationUnit = "MINUTES",
                startingPricePerSlot = 150.0,
                currency = "USD",
                unavailableDatesJson = JSONArray(listOf("2026-11-26", "2026-12-25")).toString(),
                isBiddingEnabled = true,
                minAcceptableBidPerSlot = 120.0,
                isPlatformPaymentsEnabled = true,
                refundCutoffDays = 14,
                paymentGateTimerMinutes = 60,
                additionalInfo = "Valet parking available at North Gate. Amplified music allowed until 23:00.",
                coverImageUri = "sample://ballroom",
                isDisabled = false
            )

            val loft = EventSpaceEntity(
                name = "Downtown Creative Loft",
                tags = listOf("Photo Shoots", "Workshops", "Art Exhibits", "Pop-ups"),
                description = "Modern industrial loft with authentic exposed brick, expansive south-facing sunlight, and custom modular seating.",
                spaceType = "Indoor",
                capacity = 45,
                customSpecifications = mapOf(
                    "Lighting" to "Natural Southern Daylight & Ceiling Track Dimmer Lights",
                    "Furniture" to "6 Modular Work Tables & 45 Herman Miller Chairs",
                    "Connectivity" to "Gigabit Fiber Wi-Fi 6"
                ),
                address = "240 Mercer Street, Floor 3",
                operatingWindowsJson = sampleStudioWindows,
                slotDurationAmount = 60,
                slotDurationUnit = "MINUTES",
                startingPricePerSlot = 65.0,
                currency = "USD",
                unavailableDatesJson = JSONArray(listOf("2026-12-01")).toString(),
                isBiddingEnabled = true,
                minAcceptableBidPerSlot = 50.0,
                isPlatformPaymentsEnabled = false, // Platform payments disabled -> Two-way agreement path
                refundCutoffDays = 7,
                paymentGateTimerMinutes = 30,
                additionalInfo = "Freight elevator accessible from alleyway. Quiet hours begin after 21:00.",
                coverImageUri = "sample://loft",
                isDisabled = false
            )

            dao.insertSpace(ballroom)
            dao.insertSpace(loft)
        }
    }
}

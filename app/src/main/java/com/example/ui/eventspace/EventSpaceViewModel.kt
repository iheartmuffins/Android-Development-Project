package com.example.ui.eventspace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.EventSpaceEntity
import com.example.data.model.OperatingWindow
import com.example.data.repository.EventSpaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

data class EventSpaceListUiState(
    val spaces: List<EventSpaceEntity> = emptyList(),
    val isLoading: Boolean = false
)

data class EventSpaceFormState(
    val id: Long = 0,
    val isEditMode: Boolean = false,
    val name: String = "",
    val nameError: String? = null,
    val coverImageUri: String? = null,
    val tags: List<String> = emptyList(),
    val newTagInput: String = "",
    val description: String = "",
    val spaceType: String = "Indoor", // "Indoor", "Outdoor", "Hybrid"
    val capacity: String = "50",
    val capacityError: String? = null,
    val customSpecs: List<Pair<String, String>> = emptyList(),
    val address: String = "",
    val operatingWindows: List<OperatingWindow> = listOf(
        OperatingWindow(startTime = "09:00", endTime = "17:00", daysOfWeek = listOf("MON", "TUE", "WED", "THU", "FRI"))
    ),
    val slotDurationAmount: String = "60",
    val slotDurationError: String? = null,
    val slotDurationUnit: String = "MINUTES", // "MINUTES" or "HOURS"
    val startingPricePerSlot: String = "50.0",
    val priceError: String? = null,
    val currency: String = "USD",
    val unavailableDates: List<String> = emptyList(),
    val isBiddingEnabled: Boolean = true,
    val minAcceptableBidPerSlot: String = "40.0",
    val minBidError: String? = null,
    val isPlatformPaymentsEnabled: Boolean = true,
    val refundCutoffDays: String = "7",
    val paymentGateTimerMinutes: String = "30",
    val additionalInfo: String = "",
    val isDisabled: Boolean = false,
    val isSaving: Boolean = false,
    val saveErrorMessage: String? = null
)

class EventSpaceViewModel(
    private val repository: EventSpaceRepository
) : ViewModel() {

    val listUiState: StateFlow<EventSpaceListUiState> = repository.allSpaces
        .map { spaces ->
            EventSpaceListUiState(
                spaces = spaces,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EventSpaceListUiState(isLoading = true)
        )

    private val _formState = MutableStateFlow(EventSpaceFormState())
    val formState: StateFlow<EventSpaceFormState> = _formState.asStateFlow()

    private val _selectedSpace = MutableStateFlow<EventSpaceEntity?>(null)
    val selectedSpace: StateFlow<EventSpaceEntity?> = _selectedSpace.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun selectSpace(space: EventSpaceEntity?) {
        _selectedSpace.value = space
    }

    fun toggleSpaceDisabled(space: EventSpaceEntity) {
        setSpaceDisabled(space.id, !space.isDisabled)
    }

    fun setSpaceDisabled(spaceId: Long, isDisabled: Boolean) {
        viewModelScope.launch {
            repository.toggleDisabled(spaceId, isDisabled)
            if (_selectedSpace.value?.id == spaceId) {
                _selectedSpace.value = _selectedSpace.value?.copy(isDisabled = isDisabled)
            }
        }
    }

    fun initFormForCreate() {
        _formState.value = EventSpaceFormState(
            isEditMode = false,
            operatingWindows = listOf(
                OperatingWindow(startTime = "09:00", endTime = "17:00", daysOfWeek = listOf("MON", "TUE", "WED", "THU", "FRI"))
            )
        )
    }

    fun initFormForEdit(space: EventSpaceEntity) {
        val parsedWindows = parseWindowsJson(space.operatingWindowsJson)
        val parsedDates = parseDatesJson(space.unavailableDatesJson)
        val specsList = space.customSpecifications.map { it.key to it.value }

        _formState.value = EventSpaceFormState(
            id = space.id,
            isEditMode = true,
            name = space.name,
            coverImageUri = space.coverImageUri,
            tags = space.tags,
            description = space.description,
            spaceType = space.spaceType,
            capacity = space.capacity.toString(),
            customSpecs = specsList,
            address = space.address,
            operatingWindows = if (parsedWindows.isNotEmpty()) parsedWindows else listOf(OperatingWindow()),
            slotDurationAmount = space.slotDurationAmount.toString(),
            slotDurationUnit = space.slotDurationUnit,
            startingPricePerSlot = space.startingPricePerSlot.toString(),
            currency = space.currency,
            unavailableDates = parsedDates,
            isBiddingEnabled = space.isBiddingEnabled,
            minAcceptableBidPerSlot = space.minAcceptableBidPerSlot?.toString() ?: "",
            isPlatformPaymentsEnabled = space.isPlatformPaymentsEnabled,
            refundCutoffDays = space.refundCutoffDays.toString(),
            paymentGateTimerMinutes = space.paymentGateTimerMinutes.toString(),
            additionalInfo = space.additionalInfo,
            isDisabled = space.isDisabled
        )
    }

    fun updateName(name: String) {
        _formState.value = _formState.value.copy(name = name, nameError = null)
    }

    fun updateCoverImageUri(uri: String?) {
        _formState.value = _formState.value.copy(coverImageUri = uri)
    }

    fun updateNewTagInput(tag: String) {
        _formState.value = _formState.value.copy(newTagInput = tag)
    }

    fun addTag(tag: String) {
        val trimmed = tag.trim()
        if (trimmed.isNotBlank() && !trimmed.contains(";") && !_formState.value.tags.contains(trimmed)) {
            _formState.value = _formState.value.copy(
                tags = _formState.value.tags + trimmed,
                newTagInput = ""
            )
        }
    }

    fun removeTag(tag: String) {
        _formState.value = _formState.value.copy(
            tags = _formState.value.tags - tag
        )
    }

    fun updateDescription(description: String) {
        _formState.value = _formState.value.copy(description = description)
    }

    fun updateSpaceType(type: String) {
        _formState.value = _formState.value.copy(spaceType = type)
    }

    fun updateCapacity(capacity: String) {
        _formState.value = _formState.value.copy(capacity = capacity, capacityError = null)
    }

    fun addCustomSpec(label: String = "", value: String = "") {
        _formState.value = _formState.value.copy(
            customSpecs = _formState.value.customSpecs + (label to value)
        )
    }

    fun removeCustomSpec(index: Int) {
        val current = _formState.value.customSpecs.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _formState.value = _formState.value.copy(customSpecs = current)
        }
    }

    fun updateCustomSpec(index: Int, label: String, value: String) {
        val current = _formState.value.customSpecs.toMutableList()
        if (index in current.indices) {
            current[index] = label to value
            _formState.value = _formState.value.copy(customSpecs = current)
        }
    }

    fun updateAddress(address: String) {
        _formState.value = _formState.value.copy(address = address)
    }

    fun addOperatingWindow() {
        val newWindow = OperatingWindow(startTime = "09:00", endTime = "17:00", daysOfWeek = listOf("SAT", "SUN"))
        _formState.value = _formState.value.copy(
            operatingWindows = _formState.value.operatingWindows + newWindow
        )
    }

    fun removeOperatingWindow(id: String) {
        if (_formState.value.operatingWindows.size > 1) {
            _formState.value = _formState.value.copy(
                operatingWindows = _formState.value.operatingWindows.filter { it.id != id }
            )
        }
    }

    fun updateOperatingWindow(window: OperatingWindow) {
        _formState.value = _formState.value.copy(
            operatingWindows = _formState.value.operatingWindows.map { if (it.id == window.id) window else it }
        )
    }

    fun updateSlotDurationAmount(amount: String) {
        _formState.value = _formState.value.copy(slotDurationAmount = amount, slotDurationError = null)
    }

    fun updateSlotDurationUnit(unit: String) {
        _formState.value = _formState.value.copy(slotDurationUnit = unit, slotDurationError = null)
    }

    fun updateStartingPrice(price: String) {
        _formState.value = _formState.value.copy(startingPricePerSlot = price, priceError = null)
    }

    fun updateCurrency(currency: String) {
        _formState.value = _formState.value.copy(currency = currency)
    }

    fun addUnavailableDate(dateStr: String) {
        val trimmed = dateStr.trim()
        if (trimmed.isNotBlank() && !_formState.value.unavailableDates.contains(trimmed)) {
            _formState.value = _formState.value.copy(
                unavailableDates = _formState.value.unavailableDates + trimmed
            )
        }
    }

    fun removeUnavailableDate(dateStr: String) {
        _formState.value = _formState.value.copy(
            unavailableDates = _formState.value.unavailableDates - dateStr
        )
    }

    fun toggleBidding(enabled: Boolean) {
        _formState.value = _formState.value.copy(isBiddingEnabled = enabled)
    }

    fun updateMinBid(amount: String) {
        _formState.value = _formState.value.copy(minAcceptableBidPerSlot = amount, minBidError = null)
    }

    fun togglePlatformPayments(enabled: Boolean) {
        _formState.value = _formState.value.copy(isPlatformPaymentsEnabled = enabled)
    }

    fun updateRefundCutoff(days: String) {
        _formState.value = _formState.value.copy(refundCutoffDays = days)
    }

    fun updatePaymentGateTimer(minutes: String) {
        _formState.value = _formState.value.copy(paymentGateTimerMinutes = minutes)
    }

    fun updateAdditionalInfo(info: String) {
        _formState.value = _formState.value.copy(additionalInfo = info)
    }

    fun saveSpace(onSuccess: (EventSpaceEntity) -> Unit): Boolean {
        val state = _formState.value
        var hasError = false

        // 1. Validate space name
        val nameTrimmed = state.name.trim()
        if (nameTrimmed.isBlank()) {
            _formState.value = _formState.value.copy(nameError = "Event space name is required")
            hasError = true
        }

        // 2. Validate capacity
        val capInt = state.capacity.toIntOrNull()
        if (capInt == null || capInt <= 0) {
            _formState.value = _formState.value.copy(capacityError = "Please enter a valid positive capacity")
            hasError = true
        }

        // 3. Validate slot duration (max 1440 min or 24 hrs)
        val slotAmount = state.slotDurationAmount.toIntOrNull()
        if (slotAmount == null || slotAmount <= 0) {
            _formState.value = _formState.value.copy(slotDurationError = "Enter a valid positive duration")
            hasError = true
        } else if (state.slotDurationUnit == "MINUTES" && slotAmount > 1440) {
            _formState.value = _formState.value.copy(slotDurationError = "Minutes cannot exceed 1,440 (24 hours)")
            hasError = true
        } else if (state.slotDurationUnit == "HOURS" && slotAmount > 24) {
            _formState.value = _formState.value.copy(slotDurationError = "Hours cannot exceed 24")
            hasError = true
        }

        // 4. Validate starting price
        val startingPrice = state.startingPricePerSlot.toDoubleOrNull()
        if (startingPrice == null || startingPrice < 0) {
            _formState.value = _formState.value.copy(priceError = "Enter a valid non-negative starting price")
            hasError = true
        }

        // 5. Validate min acceptable bid if bidding enabled
        var minBidDouble: Double? = null
        if (state.isBiddingEnabled && state.minAcceptableBidPerSlot.isNotBlank()) {
            minBidDouble = state.minAcceptableBidPerSlot.toDoubleOrNull()
            if (minBidDouble == null || minBidDouble < 0) {
                _formState.value = _formState.value.copy(minBidError = "Enter a valid non-negative minimum bid")
                hasError = true
            } else if (startingPrice != null && minBidDouble > startingPrice) {
                _formState.value = _formState.value.copy(minBidError = "Minimum acceptable bid should not exceed starting price")
                hasError = true
            }
        }

        if (hasError) return false

        // Serialize operating windows
        val windowsJson = JSONArray().apply {
            state.operatingWindows.forEach { w ->
                put(JSONObject().apply {
                    put("id", w.id)
                    put("startTime", w.startTime)
                    put("endTime", w.endTime)
                    put("daysOfWeek", JSONArray(w.daysOfWeek))
                })
            }
        }.toString()

        val datesJson = JSONArray(state.unavailableDates).toString()
        val specsMap = state.customSpecs
            .filter { it.first.isNotBlank() }
            .associate { it.first.trim() to it.second.trim() }

        val entity = EventSpaceEntity(
            id = state.id,
            name = nameTrimmed,
            tags = state.tags,
            description = state.description.trim(),
            spaceType = state.spaceType,
            capacity = capInt ?: 50,
            customSpecifications = specsMap,
            address = state.address.trim(),
            operatingWindowsJson = windowsJson,
            slotDurationAmount = slotAmount ?: 60,
            slotDurationUnit = state.slotDurationUnit,
            startingPricePerSlot = startingPrice ?: 50.0,
            currency = state.currency,
            unavailableDatesJson = datesJson,
            isBiddingEnabled = state.isBiddingEnabled,
            minAcceptableBidPerSlot = minBidDouble,
            isPlatformPaymentsEnabled = state.isPlatformPaymentsEnabled,
            refundCutoffDays = state.refundCutoffDays.toIntOrNull() ?: 7,
            paymentGateTimerMinutes = state.paymentGateTimerMinutes.toIntOrNull() ?: 30,
            additionalInfo = state.additionalInfo.trim(),
            coverImageUri = state.coverImageUri,
            isDisabled = state.isDisabled
        )

        _formState.value = _formState.value.copy(isSaving = true)

        viewModelScope.launch {
            if (state.isEditMode && state.id > 0) {
                repository.updateSpace(entity)
                _selectedSpace.value = entity
            } else {
                val newId = repository.insertSpace(entity)
                _selectedSpace.value = entity.copy(id = newId)
            }
            _formState.value = _formState.value.copy(isSaving = false)
            onSuccess(_selectedSpace.value ?: entity)
        }

        return true
    }

    private fun parseWindowsJson(json: String): List<OperatingWindow> {
        if (json.isBlank()) return emptyList()
        return try {
            val arr = JSONArray(json)
            val list = mutableListOf<OperatingWindow>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val daysArr = obj.optJSONArray("daysOfWeek") ?: JSONArray()
                val days = mutableListOf<String>()
                for (j in 0 until daysArr.length()) {
                    days.add(daysArr.getString(j))
                }
                list.add(
                    OperatingWindow(
                        id = obj.optString("id", i.toString()),
                        startTime = obj.optString("startTime", "09:00"),
                        endTime = obj.optString("endTime", "17:00"),
                        daysOfWeek = days
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun parseDatesJson(json: String): List<String> {
        if (json.isBlank()) return emptyList()
        return try {
            val arr = JSONArray(json)
            val list = mutableListOf<String>()
            for (i in 0 until arr.length()) {
                list.add(arr.getString(i))
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }
}

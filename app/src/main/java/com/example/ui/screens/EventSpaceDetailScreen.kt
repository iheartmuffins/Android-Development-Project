package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.EventSpaceEntity
import com.example.data.model.OperatingWindow
import com.example.ui.components.VenueCoverImage
import com.example.ui.theme.StatusActive
import com.example.ui.theme.StatusActiveContainer
import com.example.ui.theme.StatusDisabled
import kotlinx.coroutines.launch
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EventSpaceDetailScreen(
    space: EventSpaceEntity,
    onNavigateBack: () -> Unit,
    onEditSpace: () -> Unit,
    onSetDisabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isActive = !space.isDisabled

    BackHandler {
        onNavigateBack()
    }

    val windows = remember(space.operatingWindowsJson) {
        parseOperatingWindows(space.operatingWindowsJson)
    }

    val unavailableDates = remember(space.unavailableDatesJson) {
        parseStringArray(space.unavailableDatesJson)
    }

    Scaffold(
        modifier = modifier.testTag("event_space_detail_screen"),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = space.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "OWNER / SELLER MODE • View Event Space",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    // Small TopAppBar button -> Icon only
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("detail_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Small TopAppBar button -> Icon only
                    IconButton(
                        onClick = onEditSpace,
                        modifier = Modifier.testTag("detail_edit_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Event Space")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Full-width row button -> Icon + descriptive words
                    Button(
                        onClick = onEditSpace,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("detail_bottom_edit_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Edit Event Space",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // SECTION 1: Event Space Details (Read-Only Mirror)
            item {
                SectionCard(title = "1. EVENT SPACE DETAILS") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            ReadOnlyFieldLabel("Event space name")
                            Text(
                                text = space.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isActive) "Active" else "Disabled",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isActive) StatusActive else StatusDisabled
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = isActive,
                                onCheckedChange = { activeNow ->
                                    val disabledNow = !activeNow
                                    onSetDisabled(disabledNow)
                                    scope.launch {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        val msg = if (disabledNow) {
                                            "${space.name} disabled in marketplace. Confirmed bookings remain intact."
                                        } else {
                                            "${space.name} is now active in marketplace."
                                        }
                                        val result = snackbarHostState.showSnackbar(
                                            message = msg,
                                            actionLabel = "Undo",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            onSetDisabled(!disabledNow)
                                        }
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = StatusActive,
                                    checkedTrackColor = StatusActiveContainer
                                ),
                                modifier = Modifier
                                    .testTag("detail_active_switch")
                                    .semantics {
                                        contentDescription = "Toggle ${space.name} active status"
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ReadOnlyFieldLabel("Space Cover Picture")
                    Spacer(modifier = Modifier.height(6.dp))
                    VenueCoverImage(
                        coverImageUri = space.coverImageUri,
                        spaceType = space.spaceType,
                        spaceName = space.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .testTag("detail_cover_image")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ReadOnlyFieldLabel("Event Tags / Suitable Uses")
                    Spacer(modifier = Modifier.height(6.dp))
                    if (space.tags.isEmpty()) {
                        ReadOnlyMutedValue("No tags configured")
                    } else {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            space.tags.forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ReadOnlyFieldLabel("Description & Space Overview")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = space.description.ifBlank { "No description provided." },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (space.description.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // SECTION 2: Specifications (Read-Only Mirror)
            item {
                SectionCard(title = "2. SPECIFICATIONS") {
                    ReadOnlyKeyValueRow("Space Type", space.spaceType)
                    Spacer(modifier = Modifier.height(8.dp))
                    ReadOnlyKeyValueRow("Guest Capacity", "${space.capacity} people")

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                    Spacer(modifier = Modifier.height(12.dp))

                    ReadOnlyFieldLabel("Custom Specifications")
                    Spacer(modifier = Modifier.height(6.dp))
                    if (space.customSpecifications.isEmpty()) {
                        ReadOnlyMutedValue("No custom specifications added.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            space.customSpecifications.forEach { (label, value) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = value,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1.5f)
                                    )
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                            }
                        }
                    }
                }
            }

            // SECTION 3: Location (Read-Only Mirror)
            item {
                SectionCard(title = "3. LOCATION") {
                    ReadOnlyFieldLabel("Physical Address / Location Details")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = space.address.ifBlank { "No address provided." },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (space.address.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // SECTION 4: Operating Availability & Time-Slot Sizing (Read-Only Mirror)
            item {
                SectionCard(title = "4. OPERATING AVAILABILITY & TIME-SLOT SIZING") {
                    ReadOnlyFieldLabel("Operating Time Windows")
                    Spacer(modifier = Modifier.height(6.dp))

                    if (windows.isEmpty()) {
                        ReadOnlyMutedValue("No operating windows defined.")
                    } else {
                        windows.forEachIndexed { index, win ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Window ${index + 1}: ${win.startTime} – ${win.endTime}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        win.daysOfWeek.forEach { d ->
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = MaterialTheme.colorScheme.primaryContainer
                                            ) {
                                                Text(
                                                    text = d,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    ReadOnlyKeyValueRow(
                        label = "Time Slot Duration",
                        value = "${space.slotDurationAmount} ${space.slotDurationUnit}"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ReadOnlyKeyValueRow(
                        label = "Starting Price per Slot",
                        value = "${space.currency} ${space.startingPricePerSlot}"
                    )
                }
            }

            // SECTION 5: Unavailable Dates (Read-Only Mirror)
            item {
                SectionCard(title = "5. UNAVAILABLE DATES") {
                    if (unavailableDates.isEmpty()) {
                        ReadOnlyMutedValue("No blocked or unavailable dates configured.")
                    } else {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            unavailableDates.forEach { dt ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.errorContainer
                                ) {
                                    Text(
                                        text = dt,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 6: Bidding & Platform Payments (Read-Only Mirror)
            item {
                SectionCard(title = "6. BIDDING & PLATFORM PAYMENTS") {
                    ReadOnlyKeyValueRow(
                        label = "Enable Bidding",
                        value = if (space.isBiddingEnabled) "ON" else "OFF",
                        valueColor = if (space.isBiddingEnabled) StatusActive else StatusDisabled
                    )

                    if (space.isBiddingEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        ReadOnlyKeyValueRow(
                            label = "Minimum Acceptable Bid per Slot",
                            value = space.minAcceptableBidPerSlot?.let { "${space.currency} $it (Private)" } ?: "Not set"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                    Spacer(modifier = Modifier.height(12.dp))

                    ReadOnlyKeyValueRow(
                        label = "Enable Platform Payments",
                        value = if (space.isPlatformPaymentsEnabled) "ON" else "OFF",
                        valueColor = if (space.isPlatformPaymentsEnabled) MaterialTheme.colorScheme.primary else StatusDisabled
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    if (space.isPlatformPaymentsEnabled) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                ReadOnlyKeyValueRow("Refund Cutoff", "${space.refundCutoffDays} days before booking")
                                Spacer(modifier = Modifier.height(4.dp))
                                ReadOnlyKeyValueRow("Payment Gate Timer", "${space.paymentGateTimerMinutes} minutes")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• Post-cutoff voluntary cancellation: Customer receives 50% refund less holding costs; provider receives 50%.\n• Provider confirms availability commitment at cutoff to release first 50% payout.\n• Provider-caused space unusability (≥20% booked time lost & space not used): 100% full refund.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "No platform payment gateway will be opened. Final booking confirmation uses the two-way mutual agreement path where both parties press 'Agree'.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 7: Additional Information (Read-Only Mirror)
            item {
                SectionCard(title = "7. ADDITIONAL INFORMATION") {
                    ReadOnlyFieldLabel("Access rules, parking instructions, noise restrictions")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = space.additionalInfo.ifBlank { "No additional information provided." },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (space.additionalInfo.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun ReadOnlyFieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun ReadOnlyMutedValue(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun ReadOnlyKeyValueRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

private fun parseOperatingWindows(json: String): List<OperatingWindow> {
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

private fun parseStringArray(json: String): List<String> {
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

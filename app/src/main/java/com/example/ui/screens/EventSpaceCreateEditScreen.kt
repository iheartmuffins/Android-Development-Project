package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.EventSpaceEntity
import com.example.data.model.OperatingWindow
import com.example.ui.components.VenueCoverImage
import com.example.ui.components.copyPickedImageToInternalStorage
import com.example.ui.eventspace.EventSpaceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EventSpaceCreateEditScreen(
    viewModel: EventSpaceViewModel,
    onNavigateBack: () -> Unit,
    onSaveSuccess: (EventSpaceEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val persistedUri = copyPickedImageToInternalStorage(context, uri)
                viewModel.updateCoverImageUri(persistedUri)
            }
        }
    }

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        modifier = modifier.testTag("event_space_create_edit_screen"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (state.isEditMode) "Edit Event Space" else "Create Event Space",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "OWNER / SELLER MODE • Standard Space Listing",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("create_edit_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Compact TopAppBar button -> Icon only
                    IconButton(
                        onClick = {
                            val ok = viewModel.saveSpace { savedEntity ->
                                onSaveSuccess(savedEntity)
                            }
                            if (!ok) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Please correct the errors in the form before saving.")
                                }
                            }
                        },
                        enabled = !state.isSaving,
                        modifier = Modifier.testTag("top_save_space_btn")
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = if (state.isEditMode) "Save Changes" else "Create Event Space"
                            )
                        }
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
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("cancel_space_btn")
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val ok = viewModel.saveSpace { savedEntity ->
                                onSaveSuccess(savedEntity)
                            }
                            if (!ok) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Please correct the errors in the form before saving.")
                                }
                            }
                        },
                        enabled = !state.isSaving,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("submit_space_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (state.isEditMode) "Save Changes" else "Create Event Space",
                                fontWeight = FontWeight.Bold
                            )
                        }
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
            // SECTION 1: Event Space Details
            item {
                SectionCard(title = "1. EVENT SPACE DETAILS") {
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { viewModel.updateName(it) },
                        label = { Text("Event space name *") },
                        placeholder = { Text("e.g. Main Ballroom, Studio A") },
                        isError = state.nameError != null,
                        supportingText = state.nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("space_name_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Single Cover Picture Picker
                    Text(
                        text = "Space Cover Picture (1 Photo)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        VenueCoverImage(
                            coverImageUri = state.coverImageUri,
                            spaceType = state.spaceType,
                            spaceName = state.name.ifBlank { "Event Space" },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .testTag("form_cover_preview")
                        )
                        if (!state.coverImageUri.isNullOrBlank()) {
                            // Small icon button -> Icon only
                            IconButton(
                                onClick = { viewModel.updateCoverImageUri(null) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.55f))
                                    .testTag("remove_cover_photo_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove cover picture",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Full-width row button -> Icon + descriptive words
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("pick_cover_photo_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (state.coverImageUri.isNullOrBlank()) "Choose Cover Photo" else "Change Cover Photo",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Event Tags / Suitable Uses",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Suggested quick tags
                    val suggestedTags = listOf("Weddings", "Conferences", "Sports", "Workshops", "Banquets", "Photo Shoots")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        suggestedTags.forEach { tag ->
                            val isSelected = state.tags.contains(tag)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) viewModel.removeTag(tag) else viewModel.addTag(tag)
                                },
                                label = { Text(tag) },
                                leadingIcon = if (isSelected) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                } else null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Custom tag input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = state.newTagInput,
                            onValueChange = { viewModel.updateNewTagInput(it) },
                            placeholder = { Text("Add custom tag...") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("custom_tag_input"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(
                            onClick = { viewModel.addTag(state.newTagInput) },
                            enabled = state.newTagInput.isNotBlank(),
                            modifier = Modifier.height(56.dp).testTag("add_tag_btn")
                        ) {
                            Text("Add")
                        }
                    }

                    // Displayed active custom tags
                    val customTags = state.tags.filterNot { suggestedTags.contains(it) }
                    if (customTags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            customTags.forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = tag,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove tag",
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clickable { viewModel.removeTag(tag) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { viewModel.updateDescription(it) },
                        label = { Text("Description & Space Overview") },
                        placeholder = { Text("Describe the visual ambiance, layout, and amenities...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("space_description_input"),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }

            // SECTION 2: Description & Specifications
            item {
                SectionCard(title = "2. SPECIFICATIONS") {
                    // Space Type Selector
                    Text(
                        text = "Space Type",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Indoor", "Outdoor", "Hybrid").forEach { type ->
                            FilterChip(
                                selected = state.spaceType == type,
                                onClick = { viewModel.updateSpaceType(type) },
                                label = { Text(type) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Capacity
                    OutlinedTextField(
                        value = state.capacity,
                        onValueChange = { viewModel.updateCapacity(it) },
                        label = { Text("Guest Capacity (people) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.capacityError != null,
                        supportingText = state.capacityError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("space_capacity_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Custom specifications 2-column table
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Custom Specifications",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        TextButton(onClick = { viewModel.addCustomSpec("", "") }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Row")
                        }
                    }

                    if (state.customSpecs.isEmpty()) {
                        Text(
                            text = "No custom specifications added. Tap '+ Add Row' to specify features like restrooms, audio/visual gear, or kitchen setup.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        state.customSpecs.forEachIndexed { index, pair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = pair.first,
                                    onValueChange = { newLabel ->
                                        viewModel.updateCustomSpec(index, newLabel, pair.second)
                                    },
                                    placeholder = { Text("e.g. Restrooms") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = pair.second,
                                    onValueChange = { newValue ->
                                        viewModel.updateCustomSpec(index, pair.first, newValue)
                                    },
                                    placeholder = { Text("e.g. 4 private stalls") },
                                    modifier = Modifier.weight(1.5f),
                                    singleLine = true
                                )
                                IconButton(
                                    onClick = { viewModel.removeCustomSpec(index) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove row",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 3: Location
            item {
                SectionCard(title = "3. LOCATION") {
                    OutlinedTextField(
                        value = state.address,
                        onValueChange = { viewModel.updateAddress(it) },
                        label = { Text("Physical Address / Location Details") },
                        placeholder = { Text("e.g. 1400 Grand Blvd, Suite 200, City, State") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("space_address_input"),
                        minLines = 2
                    )
                }
            }

            // SECTION 4: Operating Availability & Time-Slot Sizing
            item {
                SectionCard(title = "4. OPERATING AVAILABILITY & TIME-SLOT SIZING") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Operating Time Windows",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        TextButton(onClick = { viewModel.addOperatingWindow() }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Window")
                        }
                    }

                    state.operatingWindows.forEachIndexed { index, window ->
                        OperatingWindowCard(
                            index = index,
                            window = window,
                            canRemove = state.operatingWindows.size > 1,
                            onUpdate = { viewModel.updateOperatingWindow(it) },
                            onRemove = { viewModel.removeOperatingWindow(window.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Slot Duration Configuration
                    Text(
                        text = "Time Slot Duration",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Duration of each generated bookable slot (Minutes max 1,440; Hours max 24).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        OutlinedTextField(
                            value = state.slotDurationAmount,
                            onValueChange = { viewModel.updateSlotDurationAmount(it) },
                            label = { Text("Amount *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = state.slotDurationError != null,
                            supportingText = state.slotDurationError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("slot_duration_amount_input"),
                            singleLine = true
                        )

                        // Duration Unit Dropdown (Minutes / Hours)
                        var unitExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = unitExpanded,
                            onExpandedChange = { unitExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = state.slotDurationUnit,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Type") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = unitExpanded,
                                onDismissRequest = { unitExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("MINUTES") },
                                    onClick = {
                                        viewModel.updateSlotDurationUnit("MINUTES")
                                        unitExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("HOURS") },
                                    onClick = {
                                        viewModel.updateSlotDurationUnit("HOURS")
                                        unitExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Starting Price per Slot & Currency
                    Text(
                        text = "Starting Price per Slot (Advertised Rate)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        OutlinedTextField(
                            value = state.startingPricePerSlot,
                            onValueChange = { viewModel.updateStartingPrice(it) },
                            label = { Text("Starting Price *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = state.priceError != null,
                            supportingText = state.priceError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("starting_price_input"),
                            singleLine = true
                        )

                        var currencyExpanded by remember { mutableStateOf(false) }
                        val currencies = listOf("USD", "EUR", "GBP", "CAD", "AUD", "INR")
                        ExposedDropdownMenuBox(
                            expanded = currencyExpanded,
                            onExpandedChange = { currencyExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = state.currency,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Currency") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = currencyExpanded,
                                onDismissRequest = { currencyExpanded = false }
                            ) {
                                currencies.forEach { curr ->
                                    DropdownMenuItem(
                                        text = { Text(curr) },
                                        onClick = {
                                            viewModel.updateCurrency(curr)
                                            currencyExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 5: Unavailable Dates
            item {
                SectionCard(title = "5. UNAVAILABLE DATES") {
                    Text(
                        text = "Dates when this space is completely closed (e.g. maintenance or holidays).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    var newDateInput by remember { mutableStateOf("") }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newDateInput,
                            onValueChange = { newDateInput = it },
                            placeholder = { Text("YYYY-MM-DD (e.g. 2026-12-25)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(
                            onClick = {
                                if (newDateInput.isNotBlank()) {
                                    viewModel.addUnavailableDate(newDateInput)
                                    newDateInput = ""
                                }
                            },
                            enabled = newDateInput.isNotBlank(),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text("Block")
                        }
                    }

                    if (state.unavailableDates.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            state.unavailableDates.forEach { dateStr ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.errorContainer
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = dateStr,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove date",
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clickable { viewModel.removeUnavailableDate(dateStr) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 6: Bidding & Platform Payments
            item {
                SectionCard(title = "6. BIDDING & PLATFORM PAYMENTS") {
                    // Bidding Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Enable Bidding",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Allows customers to submit custom price proposals per slot.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = state.isBiddingEnabled,
                            onCheckedChange = { viewModel.toggleBidding(it) },
                            modifier = Modifier.testTag("enable_bidding_switch")
                        )
                    }

                    AnimatedVisibility(visible = state.isBiddingEnabled) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            OutlinedTextField(
                                value = state.minAcceptableBidPerSlot,
                                onValueChange = { viewModel.updateMinBid(it) },
                                label = { Text("Minimum Acceptable Bid per Slot (${state.currency})") },
                                placeholder = { Text("Private rejection threshold") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                isError = state.minBidError != null,
                                supportingText = {
                                    if (state.minBidError != null) {
                                        Text(state.minBidError!!, color = MaterialTheme.colorScheme.error)
                                    } else {
                                        Text("Private threshold. Bids below this are auto-rejected after 2 minutes with a generic rejection notification.")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("min_bid_input"),
                                singleLine = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Platform Payments Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Enable Platform Payments",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (state.isPlatformPaymentsEnabled) "Online gateway confirmation with refund/payout rules."
                                else "Direct two-way agreement path without online payment gate.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = state.isPlatformPaymentsEnabled,
                            onCheckedChange = { viewModel.togglePlatformPayments(it) },
                            modifier = Modifier.testTag("platform_payments_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (state.isPlatformPaymentsEnabled) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Payment Policy & Safeguards",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = state.refundCutoffDays,
                                        onValueChange = { viewModel.updateRefundCutoff(it) },
                                        label = { Text("Refund Cutoff (Days)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = state.paymentGateTimerMinutes,
                                        onValueChange = { viewModel.updatePaymentGateTimer(it) },
                                        label = { Text("Gate Timer (Mins)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }
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

            // SECTION 7: Additional Information
            item {
                SectionCard(title = "7. ADDITIONAL INFORMATION") {
                    OutlinedTextField(
                        value = state.additionalInfo,
                        onValueChange = { viewModel.updateAdditionalInfo(it) },
                        label = { Text("Access rules, parking instructions, noise restrictions") },
                        placeholder = { Text("Enter supplementary operational guidance...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("space_additional_info_input"),
                        minLines = 3,
                        maxLines = 6
                    )
                }
            }

            // Bottom space for scrolling above bottom bar
            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OperatingWindowCard(
    index: Int,
    window: OperatingWindow,
    canRemove: Boolean,
    onUpdate: (OperatingWindow) -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Window ${index + 1}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (canRemove) {
                    IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove window",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // From / To pickers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = window.startTime,
                    onValueChange = { onUpdate(window.copy(startTime = it)) },
                    label = { Text("From") },
                    placeholder = { Text("09:00") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = window.endTime,
                    onValueChange = { onUpdate(window.copy(endTime = it)) },
                    label = { Text("To") },
                    placeholder = { Text("17:00") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Weekday pills: MON, TUE, WED, THU, FRI, SAT, SUN
            val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                days.forEach { day ->
                    val isSelected = window.daysOfWeek.contains(day)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val updatedDays = if (isSelected) window.daysOfWeek - day else window.daysOfWeek + day
                            onUpdate(window.copy(daysOfWeek = updatedDays))
                        },
                        label = { Text(day, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
    }
}

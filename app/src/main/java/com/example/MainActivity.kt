package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AppDatabase
import com.example.data.repository.EventSpaceRepository
import com.example.ui.eventspace.EventSpaceViewModel
import com.example.ui.screens.EventSpaceCreateEditScreen
import com.example.ui.screens.EventSpaceDetailScreen
import com.example.ui.screens.ProviderSpacesScreen
import com.example.ui.theme.VenueOpsTheme

sealed class VenueScreen {
    object SpacesList : VenueScreen()
    object CreateEditSpace : VenueScreen()
    data class SpaceDetail(val spaceId: Long) : VenueScreen()
}

class MainActivity : ComponentActivity() {

    private val repository by lazy {
        val database = AppDatabase.getInstance(applicationContext)
        EventSpaceRepository(database.eventSpaceDao())
    }

    private val viewModel: EventSpaceViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventSpaceViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VenueOpsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    VenueAppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun VenueAppNavigation(viewModel: EventSpaceViewModel) {
    var currentScreen by remember { mutableStateOf<VenueScreen>(VenueScreen.SpacesList) }
    var returnToDetailOnCancel by remember { mutableStateOf(false) }
    val selectedSpace by viewModel.selectedSpace.collectAsStateWithLifecycle()

    Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
            is VenueScreen.SpacesList -> {
                ProviderSpacesScreen(
                    viewModel = viewModel,
                    onCreateSpace = {
                        viewModel.selectSpace(null)
                        viewModel.initFormForCreate()
                        returnToDetailOnCancel = false
                        currentScreen = VenueScreen.CreateEditSpace
                    },
                    onSelectSpace = { space ->
                        viewModel.selectSpace(space)
                        currentScreen = VenueScreen.SpaceDetail(space.id)
                    },
                    onEditSpace = { space ->
                        viewModel.selectSpace(space)
                        viewModel.initFormForEdit(space)
                        returnToDetailOnCancel = false
                        currentScreen = VenueScreen.CreateEditSpace
                    }
                )
            }
            is VenueScreen.CreateEditSpace -> {
                EventSpaceCreateEditScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        currentScreen = if (returnToDetailOnCancel && selectedSpace != null) {
                            VenueScreen.SpaceDetail(selectedSpace!!.id)
                        } else {
                            viewModel.selectSpace(null)
                            VenueScreen.SpacesList
                        }
                    },
                    onSaveSuccess = { savedEntity ->
                        viewModel.selectSpace(savedEntity)
                        currentScreen = VenueScreen.SpaceDetail(savedEntity.id)
                    }
                )
            }
            is VenueScreen.SpaceDetail -> {
                selectedSpace?.let { space ->
                    EventSpaceDetailScreen(
                        space = space,
                        onNavigateBack = {
                            viewModel.selectSpace(null)
                            currentScreen = VenueScreen.SpacesList
                        },
                        onEditSpace = {
                            viewModel.initFormForEdit(space)
                            returnToDetailOnCancel = true
                            currentScreen = VenueScreen.CreateEditSpace
                        },
                        onSetDisabled = { isDisabled ->
                            viewModel.setSpaceDisabled(space.id, isDisabled)
                        }
                    )
                } ?: run {
                    ProviderSpacesScreen(
                        viewModel = viewModel,
                        onCreateSpace = {
                            viewModel.selectSpace(null)
                            viewModel.initFormForCreate()
                            returnToDetailOnCancel = false
                            currentScreen = VenueScreen.CreateEditSpace
                        },
                        onSelectSpace = { space ->
                            viewModel.selectSpace(space)
                            currentScreen = VenueScreen.SpaceDetail(space.id)
                        },
                        onEditSpace = { space ->
                            viewModel.selectSpace(space)
                            viewModel.initFormForEdit(space)
                            returnToDetailOnCancel = false
                            currentScreen = VenueScreen.CreateEditSpace
                        }
                    )
                }
            }
        }
    }
}

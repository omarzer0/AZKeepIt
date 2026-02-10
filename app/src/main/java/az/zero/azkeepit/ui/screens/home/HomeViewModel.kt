package az.zero.azkeepit.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val isEditModeEnabled = MutableStateFlow(false)
    private val numberOfSelectedItemsForAction = MutableStateFlow(0)

    fun onEditModeChange(isActive: Boolean) {
        viewModelScope.launch {
            isEditModeEnabled.emit(isActive)
            if (isActive.not()) numberOfSelectedItemsForAction.emit(0)
        }
    }

    fun onNumberOfSelectedItemsForActionChange(newSelectedNumber: Int) {
        viewModelScope.launch {
            numberOfSelectedItemsForAction.emit(newSelectedNumber)
        }
    }

    val state = combine(
        isEditModeEnabled,
        numberOfSelectedItemsForAction
    ) { isEditModeEnabled,
        numberOfSelectedItemsForAction ->
        HomeScreenState(
            isEditModeEnabled = isEditModeEnabled,
            numberOfSelectedItemsForAction = numberOfSelectedItemsForAction
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeScreenState()
    )

}

data class HomeScreenState(
    val isEditModeEnabled: Boolean = false,
    val numberOfSelectedItemsForAction: Int = 0
)
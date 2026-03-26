package com.rago.tempo.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rago.tempo.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onLogoutClicked() {
        viewModelScope.launch {
            logoutUseCase().collectLatest { result ->
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(isLoggedOut = true)
                }
            }
        }
    }
}

data class HomeUiState(
    val isLoggedOut: Boolean = false
)

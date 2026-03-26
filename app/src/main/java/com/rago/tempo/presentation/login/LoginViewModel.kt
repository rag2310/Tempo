package com.rago.tempo.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rago.tempo.domain.model.User
import com.rago.tempo.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            loginUseCase(_uiState.value.email, _uiState.value.password).collectLatest { result ->
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = _uiState.value.copy(
                            user = user,
                            isLoading = false,
                            isLoggedIn = true
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            error = exception.message ?: "Unknown error",
                            isLoading = false
                        )
                    }
                )
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val user: User? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

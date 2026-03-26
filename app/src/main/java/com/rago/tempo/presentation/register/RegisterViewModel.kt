package com.rago.tempo.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rago.tempo.domain.model.User
import com.rago.tempo.domain.usecase.RegisterUseCase
import com.rago.tempo.util.FieldValidator
import com.rago.tempo.util.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name, nameError = null)
    }

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
    }

    fun onPasswordVisibilityToggle() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun resetState() {
        _uiState.value = RegisterUiState()
    }

    fun onRegisterClicked() {
        val nameRes = FieldValidator.validateName(_uiState.value.name)
        val emailRes = FieldValidator.validateEmail(_uiState.value.email)
        val passwordRes = FieldValidator.validatePassword(_uiState.value.password)

        if (nameRes is ValidationResult.Error || emailRes is ValidationResult.Error || passwordRes is ValidationResult.Error) {
            _uiState.value = _uiState.value.copy(
                nameError = (nameRes as? ValidationResult.Error)?.messageRes,
                emailError = (emailRes as? ValidationResult.Error)?.messageRes,
                passwordError = (passwordRes as? ValidationResult.Error)?.messageRes
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            registerUseCase(
                _uiState.value.name,
                _uiState.value.email,
                _uiState.value.password
            ).collectLatest { result ->
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = _uiState.value.copy(
                            user = user,
                            isLoading = false,
                            isRegistered = true
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

data class RegisterUiState(
    val name: String = "",
    val nameError: Int? = null,
    val email: String = "",
    val emailError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val user: User? = null,
    val isRegistered: Boolean = false,
    val error: String? = null
)

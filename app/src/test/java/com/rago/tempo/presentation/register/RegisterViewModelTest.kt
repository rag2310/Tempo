package com.rago.tempo.presentation.register

import app.cash.turbine.test
import com.rago.tempo.domain.model.User
import com.rago.tempo.domain.usecase.RegisterUseCase
import com.rago.tempo.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val registerUseCase: RegisterUseCase = mockk()
    private lateinit var viewModel: RegisterViewModel

    @Test
    fun `initial state should be idle`() = runTest {
        viewModel = RegisterViewModel(registerUseCase)
        assertEquals(RegisterUiState(), viewModel.uiState.value)
    }

    @Test
    fun `when registration is successful, state should update to success`() = runTest {
        // Arrange
        val user = User("test@example.com", "token", "Test User")
        coEvery { registerUseCase("Test User", "test@example.com", "password") } returns flowOf(Result.success(user))
        viewModel = RegisterViewModel(registerUseCase)

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(RegisterUiState(), awaitItem())
            
            viewModel.onNameChanged("Test User")
            assertEquals("Test User", awaitItem().name)

            viewModel.onEmailChanged("test@example.com")
            assertEquals("test@example.com", awaitItem().email)

            viewModel.onPasswordChanged("password")
            assertEquals("password", awaitItem().password)

            viewModel.onRegisterClicked()
            
            assertEquals(true, awaitItem().isLoading)
            
            val finalState = awaitItem()
            assertEquals(false, finalState.isLoading)
            assertEquals(user, finalState.user)
            assertEquals(true, finalState.isRegistered)
        }
    }

    @Test
    fun `when registration fails, state should update to error`() = runTest {
        // Arrange
        val errorMessage = "Registration failed"
        coEvery { registerUseCase("Test User", "test@example.com", "password") } returns flowOf(Result.failure(Exception(errorMessage)))
        viewModel = RegisterViewModel(registerUseCase)

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(RegisterUiState(), awaitItem())
            
            viewModel.onNameChanged("Test User")
            awaitItem()

            viewModel.onEmailChanged("test@example.com")
            awaitItem()

            viewModel.onPasswordChanged("password")
            awaitItem()

            viewModel.onRegisterClicked()
            
            assertEquals(true, awaitItem().isLoading)
            
            val finalState = awaitItem()
            assertEquals(false, finalState.isLoading)
            assertEquals(errorMessage, finalState.error)
        }
    }
}

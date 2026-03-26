package com.rago.tempo.presentation.login

import app.cash.turbine.test
import com.rago.tempo.domain.model.User
import com.rago.tempo.domain.usecase.LoginUseCase
import com.rago.tempo.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase: LoginUseCase = mockk()
    private lateinit var viewModel: LoginViewModel

    @Test
    fun `initial state should be idle`() = runTest {
        viewModel = LoginViewModel(loginUseCase)
        assertEquals(LoginUiState(), viewModel.uiState.value)
    }

    @Test
    fun `when login is successful, state should update to success`() = runTest {
        // Arrange
        val user = User("test@example.com", "token", "Test User")
        coEvery { loginUseCase("test@example.com", "password") } returns flowOf(Result.success(user))
        viewModel = LoginViewModel(loginUseCase)

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(LoginUiState(), awaitItem())
            
            viewModel.onEmailChanged("test@example.com")
            assertEquals("test@example.com", awaitItem().email)

            viewModel.onPasswordChanged("password")
            assertEquals("password", awaitItem().password)

            viewModel.onLoginClicked()
            
            assertEquals(true, awaitItem().isLoading)
            
            val finalState = awaitItem()
            assertEquals(false, finalState.isLoading)
            assertEquals(user, finalState.user)
            assertEquals(true, finalState.isLoggedIn)
        }
    }

    @Test
    fun `when login fails, state should update to error`() = runTest {
        // Arrange
        val errorMessage = "Invalid credentials"
        coEvery { loginUseCase("test@example.com", "password123") } returns flowOf(Result.failure(Exception(errorMessage)))
        viewModel = LoginViewModel(loginUseCase)

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(LoginUiState(), awaitItem())
            
            viewModel.onEmailChanged("test@example.com")
            awaitItem()

            viewModel.onPasswordChanged("password123")
            awaitItem()

            viewModel.onLoginClicked()
            
            assertEquals(true, awaitItem().isLoading)
            
            val finalState = awaitItem()
            assertEquals(false, finalState.isLoading)
            assertEquals(errorMessage, finalState.error)
        }
    }
}

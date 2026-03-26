package com.rago.tempo.data.remote

import com.rago.tempo.domain.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor() {
    fun login(email: String, password: String): Flow<Result<User>> = flow {
        delay(1000) // Simulate network delay
        if (email == "test@example.com" && password == "password") {
            emit(Result.success(User(email, "mock_token_123", "Test User")))
        } else {
            emit(Result.failure(Exception("Invalid credentials")))
        }
    }

    fun register(name: String, email: String, password: String): Flow<Result<User>> = flow {
        delay(1000) // Simulate network delay
        emit(Result.success(User(email, "mock_token_reg_123", name)))
    }
}

package com.rago.tempo.domain.repository

import com.rago.tempo.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Result<User>>
    fun register(name: String, email: String, password: String): Flow<Result<User>>
    fun logout(): Flow<Result<Unit>>
    fun getSession(): Flow<User?>
}

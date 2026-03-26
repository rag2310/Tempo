package com.rago.tempo.data.repository

import com.rago.tempo.data.remote.AuthRemoteDataSource
import com.rago.tempo.domain.model.User
import com.rago.tempo.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override fun login(email: String, password: String): Flow<Result<User>> =
        remoteDataSource.login(email, password)
}

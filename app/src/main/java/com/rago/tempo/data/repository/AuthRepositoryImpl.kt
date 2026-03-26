package com.rago.tempo.data.repository

import com.rago.tempo.data.local.dao.UserDao
import com.rago.tempo.data.local.entities.UserEntity
import com.rago.tempo.data.remote.AuthRemoteDataSource
import com.rago.tempo.domain.model.User
import com.rago.tempo.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val userDao: UserDao
) : AuthRepository {
    override fun login(email: String, password: String): Flow<Result<User>> =
        remoteDataSource.login(email, password).onEach { result ->
            result.getOrNull()?.let { user ->
                userDao.insertUser(UserEntity(user.email, user.name, user.token))
            }
        }

    override fun register(name: String, email: String, password: String): Flow<Result<User>> =
        remoteDataSource.register(name, email, password).onEach { result ->
            result.getOrNull()?.let { user ->
                userDao.insertUser(UserEntity(user.email, user.name, user.token))
            }
        }

    override fun logout(): Flow<Result<Unit>> = flow {
        userDao.clearUser()
        emit(Result.success(Unit))
    }

    override fun getSession(): Flow<User?> =
        userDao.getUser().map { entity ->
            entity?.let { User(it.email, it.token, it.name) }
        }
}

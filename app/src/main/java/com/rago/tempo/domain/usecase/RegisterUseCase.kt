package com.rago.tempo.domain.usecase

import com.rago.tempo.domain.model.User
import com.rago.tempo.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(name: String, email: String, password: String): Flow<Result<User>> =
        repository.register(name, email, password)
}

package com.rago.tempo.domain.usecase

import com.rago.tempo.domain.model.User
import com.rago.tempo.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Result<User>> =
        repository.login(email, password)
}

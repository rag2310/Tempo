package com.rago.tempo.domain.usecase

import com.rago.tempo.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Result<Unit>> = repository.logout()
}

package com.rago.tempo.domain.usecase

import com.rago.tempo.domain.model.User
import com.rago.tempo.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<User?> = repository.getSession()
}

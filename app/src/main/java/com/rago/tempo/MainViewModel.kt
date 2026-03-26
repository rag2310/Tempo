package com.rago.tempo

import androidx.lifecycle.ViewModel
import com.rago.tempo.domain.usecase.GetSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val getSessionUseCase: GetSessionUseCase
) : ViewModel()

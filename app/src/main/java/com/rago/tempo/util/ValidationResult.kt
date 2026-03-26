package com.rago.tempo.util

import androidx.annotation.StringRes

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(@StringRes val messageRes: Int) : ValidationResult()
}

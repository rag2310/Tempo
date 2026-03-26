package com.rago.tempo.util

import com.rago.tempo.R

object FieldValidator {

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun validateName(name: String): ValidationResult {
        return if (name.isBlank()) {
            ValidationResult.Error(R.string.error_field_empty)
        } else {
            ValidationResult.Success
        }
    }

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error(R.string.error_field_empty)
            !EMAIL_REGEX.matches(email) -> ValidationResult.Error(R.string.error_invalid_email)
            else -> ValidationResult.Success
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error(R.string.error_field_empty)
            password.length < 6 -> ValidationResult.Error(R.string.error_password_too_short)
            else -> ValidationResult.Success
        }
    }
}

package com.example.auth.domain

data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasUppercase: Boolean = false,
    val hasNumber: Boolean = false,
    val hasLowerCase: Boolean = false,
) {
    val isValidPassword: Boolean
        get() = hasMinLength && hasUppercase && hasNumber && hasLowerCase
}

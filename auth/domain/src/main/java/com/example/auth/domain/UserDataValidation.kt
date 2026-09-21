package com.example.auth.domain

class UserDataValidator(private val patternValidator: PatternValidator)  {

    fun isEmailValid(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }
    fun isPasswordValid(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH;
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasNumber = password.any{ it.isDigit() }

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasUppercase = hasUppercase,
            hasLowerCase = hasLowercase,
            hasNumber = hasNumber
        )
    }

    companion object{
        const val MIN_PASSWORD_LENGTH = 9
    }
}
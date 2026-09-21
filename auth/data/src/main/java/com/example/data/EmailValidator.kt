package com.example.data

import android.util.Patterns
import com.example.auth.domain.PatternValidator

object EmailValidator : PatternValidator {
    override fun matches(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
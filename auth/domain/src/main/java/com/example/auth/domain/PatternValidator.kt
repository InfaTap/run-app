package com.example.auth.domain

interface PatternValidator {
    fun matches(email: String): Boolean

}
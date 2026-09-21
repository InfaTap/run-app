package com.example.auth.domain

import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyDataResult

interface AuthRepository {
    suspend fun register(email: String, password: String): EmptyDataResult<DataError.Network>
}
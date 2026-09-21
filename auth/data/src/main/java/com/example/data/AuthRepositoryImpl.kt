package com.example.data

import com.example.auth.domain.AuthRepository
import com.example.core.data.networking.post
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post

class AuthRepositoryImpl(
    private val httpClient: HttpClient
): AuthRepository {
    override suspend fun register(
        email: String,
        password: String
    ): EmptyDataResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }

}
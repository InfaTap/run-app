package com.example.data

import com.example.auth.domain.AuthRepository
import com.example.core.data.networking.post
import com.example.core.domain.AuthInfo
import com.example.core.domain.SessionStorage
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyDataResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
): AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): EmptyDataResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )
        if (result is Result.Success) {
            sessionStorage.setAuthInfo(AuthInfo(
                accessToken = result.data.accessToken,
                refreshToken = result.data.refreshToken,
                userId = result.data.userId.toString()
            ))
        }

        return result.asEmptyDataResult()
    }

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
package com.example.core.domain

interface SessionStorage {

    suspend fun getAuthInfo(): AuthInfo?
    suspend fun setAuthInfo(authInfo: AuthInfo?)

}
package com.example.core.domain.util

sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION_ERROR,
        UNKNOWN_ERROR
    }

    enum class Local: DataError {
        DATABASE_ERROR,
        SERIALIZATION_ERROR,
        UNKNOWN_ERROR,
        DISK_FULL
    }
}
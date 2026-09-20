package com.example.core.presentation.ui

import com.example.core.domain.util.DataError

fun DataError.asUiText(): UiText {
    return when(this) {
        is DataError.Network -> {
            when(this) {
                DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(id = R.string.error_request_timeout)
                DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(id = R.string.error_too_many_requests)
                DataError.Network.NO_INTERNET -> UiText.StringResource(id = R.string.error_no_internet)
                DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(id = R.string.error_payload_too_large)
                DataError.Network.SERVER_ERROR -> UiText.StringResource(id = R.string.error_server_error)
                DataError.Network.SERIALIZATION_ERROR -> UiText.StringResource(id = R.string.error_serialization_error)
                DataError.Network.UNKNOWN_ERROR -> UiText.StringResource(id = R.string.error_unknown_error)
                else -> {
                    UiText.StringResource(id = R.string.error_unknown_error)
                }
            }
        }
        is DataError.Local -> {
            when(this) {
                DataError.Local.DATABASE_ERROR -> UiText.StringResource(id = R.string.error_database_error)
                DataError.Local.SERIALIZATION_ERROR -> UiText.StringResource(id = R.string.error_serialization_error)
                DataError.Local.UNKNOWN_ERROR -> UiText.StringResource(id = R.string.error_unknown_error)
                DataError.Local.DISK_FULL -> UiText.StringResource(id = R.string.error_disk_full)
            }
        }
    }
}
package com.example.run.network

import com.example.core.domain.location.Location
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import kotlin.time.Duration

@Serializable
data class RunDto(
    val id: String?,
    val durationMillis: Long,
    val dateTimeUtc: String,
    val distanceInMeters: Int,
    val lat: Double,
    val long: Double,
    val maxSpeedKmh: Double,
    val avgSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?
)
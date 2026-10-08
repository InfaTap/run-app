package com.example.run.presentation.run_overview.mappers

import com.example.core.domain.run.Run
import com.example.run.presentation.formatted
import com.example.run.presentation.formattedMeters
import com.example.run.presentation.run_overview.model.RunUi
import com.example.run.presentation.toFormattedKm
import com.example.run.presentation.toFormattedKmh
import com.example.run.presentation.toFormattedPace
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi{
    val localTime = dateTimeUtc.withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mma").format(localTime)

    val distanceKM = distanceInMeters/ 1000.0

    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        datetime = formattedDateTime,
        distance = distanceKM.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKm.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKM),
        totalElevation = totalElevationInMeters.formattedMeters(),
        mapPictureUrl = mapPictureUrl

    )

}
package com.example.core.database.mapper

import com.example.core.database.entity.RunEntity
import com.example.core.domain.location.Location
import com.example.core.domain.run.Run
import org.bson.types.ObjectId
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunEntity.toRun(): Run{
        return Run(
            id = id,
            duration = durationMillis.milliseconds,
            dateTimeUtc = Instant.parse(dateTimeUtc).atZone(ZoneId.of("UTC")),
            distanceInMeters = distanceMeters,
            location = Location(lat = latitude, long = longitude),
            maxSpeedKm = maxSpeedKmh,
            totalElevationInMeters = totalElevationInMeters,
            mapPictureUrl = mapPictureUrl
        )
}

fun Run.toEntity(): RunEntity{
    return RunEntity(
        durationMillis = duration.inWholeMilliseconds,
        distanceMeters = distanceInMeters,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
        latitude = location.lat,
        longitude = location.long,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKm,
        totalElevationInMeters = totalElevationInMeters,
        mapPictureUrl = mapPictureUrl,
        id = ObjectId().toHexString()
    )
}
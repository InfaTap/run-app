package com.example.core.domain

import com.example.core.domain.location.LocationTimestamp
import kotlin.math.roundToInt

object LocationDataCalculator {

    fun getDistanceInMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations.sumOf { timestamps ->
            timestamps.zipWithNext { location1, location2 ->
                location1.location.location.distanceTo(location2.location.location)
            }.sum().roundToInt()
        }
    }
}
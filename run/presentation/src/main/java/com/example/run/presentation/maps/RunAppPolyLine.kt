package com.example.run.presentation.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.core.domain.location.LocationTimestamp
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline

@Composable
fun RunAppPolyLine(
    modifier: Modifier = Modifier,
    locations: List<List<LocationTimestamp>>
) {

    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { timestamp1, timestamp2 ->
                PolyLineUi(
                    location1 = timestamp1.location.location,
                    location2 = timestamp2.location.location,
                    color = PolylineColorCalculator.locationToColor(timestamp1,timestamp2)
                )
            }
        }
    }
    polylines.forEach { polylines ->
        polylines.forEach { polyline->
            Polyline(
                points = listOf(
                    LatLng(polyline.location1.lat,polyline.location1.long),
                    LatLng(polyline.location2.lat,polyline.location2.long)
                ),
                color = polyline.color,
                jointType = JointType.BEVEL
            )
        }
    }
}
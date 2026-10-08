package com.example.run.presentation.run_overview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.example.core.domain.location.Location
import com.example.core.domain.run.Run
import com.example.core.presentation.designsystem.CalendarIcon
import com.example.core.presentation.designsystem.RunOutlinedIcon
import com.example.core.presentation.designsystem.RunTheme
import com.example.run.presentation.run_overview.mappers.toRunUi
import com.example.run.presentation.run_overview.model.RunDataUi
import com.example.run.presentation.run_overview.model.RunUi
import java.time.ZonedDateTime
import kotlin.math.max
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun RunListItem(
    runUi: RunUi,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var showDropdown by remember {
        mutableStateOf(false)
    }
    Box { Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(15.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    showDropdown = !showDropdown
                }
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),

        ) {
        MapImage(imageUrl = runUi.mapPictureUrl)
        RunningTimeSection(runUi.duration, modifier = Modifier.fillMaxWidth())
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f)
        )
        RunningDateSection(dateTime = runUi.datetime)
        DataGrid(runUi = runUi, modifier = Modifier.fillMaxWidth())


    }}
    DropdownMenu(
        expanded = showDropdown,
        onDismissRequest = {showDropdown = false}
    ) {
        DropdownMenuItem(
            text = {Text("Delete")},
            onClick = {
                showDropdown = false
                onDeleteClick()
            }
        )
    }


}

@Composable
fun RunningDateSection(
    dateTime: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = CalendarIcon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = dateTime, color = MaterialTheme.colorScheme.onSurfaceVariant)


    }
}

@Composable
private fun DataGrid(
    runUi: RunUi,
    modifier: Modifier = Modifier
) {

    val runDataUiList = listOf(
        RunDataUi(
            name = "Distance",
            value = runUi.distance
        ),
        RunDataUi(
            name = "Pace",
            value = runUi.pace
        ),
        RunDataUi(
            name = "Ave Speed",
            value = runUi.avgSpeed
        ),
        RunDataUi(
            name = "Max Speed",
            value = runUi.maxSpeed
        ),
        RunDataUi(
            name = "Total Elevation",
            value = runUi.totalElevation
        )

    )
    var maxWidth by remember {
        mutableIntStateOf(0)
    }
    val maxWidthDp = with(LocalDensity.current) { maxWidth.toDp() }
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        runDataUiList.forEach { run ->
            DataGridCell(
                runData = run,
                modifier = Modifier
                    .defaultMinSize(minWidth = maxWidthDp)
                    .onSizeChanged {
                        maxWidth = max(maxWidth, it.width)
                    })
        }
    }
}

@Composable
private fun RunningTimeSection(duration: String, modifier: Modifier = Modifier) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = RunOutlinedIcon,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total Running Time",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = duration,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }


}

@Composable
private fun MapImage(imageUrl: String?, modifier: Modifier = Modifier) {

    SubcomposeAsyncImage(
        model = imageUrl,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
            .clip(RoundedCornerShape(15.dp)),
        contentDescription = null,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.errorContainer),

                contentAlignment = Alignment.Center,

                ) {
                Text(
                    "Could not load colors",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )

}

@Composable
private fun DataGridCell(
    runData: RunDataUi,
    modifier: Modifier = Modifier
) {

    Column(modifier) {
        Text(
            runData.name,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            runData.value,
            color = MaterialTheme.colorScheme.onSurface,

            )
    }

}

@Preview
@Composable
private fun RunListItemView() {
    RunTheme {
        RunListItem(
            runUi = Run(
                id = "1",
                duration = 10.minutes + 30.seconds,
                dateTimeUtc = ZonedDateTime.now(),
                distanceInMeters = 10000,
                maxSpeedKm = 10.0,
                location = Location(
                    lat = 0.0,
                    long = 0.0
                ),
                totalElevationInMeters = 100,
                mapPictureUrl = null
            ).toRunUi(),
            onDeleteClick = {}
        )
    }
}
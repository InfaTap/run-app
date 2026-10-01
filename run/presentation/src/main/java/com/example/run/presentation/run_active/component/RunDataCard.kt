package com.example.run.presentation.run_active.component

import android.text.Layout
import androidx.annotation.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.presentation.designsystem.RunTheme
import com.example.run.domain.RunData
import com.example.run.presentation.formatted
import com.example.run.presentation.toFormattedKm
import com.example.run.presentation.toFormattedPace
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun RunDataCard(
    elapsedTime: Duration,
    runData: RunData,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .clip(RoundedCornerShape(16.dp))

        .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally) {

        RunDataItem(
            title = "Duration",
            value = elapsedTime.formatted(),
            valueFontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically) {


            RunDataItem(title = "Distance",
                modifier = Modifier.defaultMinSize(minWidth = 75.dp),
                value = (runData.distanceMeters / 1000.0).toFormattedKm()
            )
            RunDataItem(title = "Pace",
                value = (elapsedTime.toFormattedPace(runData.distanceMeters / 1000.0) )
            )
        }
    }
}

@Composable
private fun RunDataItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    valueFontSize: TextUnit = 16.sp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )
        Text(
            text = value,
            fontSize = valueFontSize,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun RunDataCardPreview() {
    RunTheme {
        RunDataCard(
            elapsedTime = 10.minutes,
            runData = RunData(
                distanceMeters = 12434,
                pace = 3.minutes,

            )
        )
    }
    
}
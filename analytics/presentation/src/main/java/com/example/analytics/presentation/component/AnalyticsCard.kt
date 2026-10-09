package com.example.analytics.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.presentation.designsystem.RunTheme

@Composable
fun AnalyticsCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier

            .clip(RoundedCornerShape(15.dp))
            .background(
                MaterialTheme.colorScheme.surface
            ).padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp)

        Text(value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp)
    }
}

@Preview
@Composable
private fun AnalyticsCardPre() {
    RunTheme{
        AnalyticsCard("Distance","121.1")
    }
}
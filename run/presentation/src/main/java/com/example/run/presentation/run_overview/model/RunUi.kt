package com.example.run.presentation.run_overview.model

import kotlin.time.Duration

data class RunUi(
    val id: String,
    val duration: String,
    val datetime: String,
    val distance: String,
    val avgSpeed: String,
    val maxSpeed: String,
    val pace: String,
    val totalElevation: String,
    val mapPictureUrl: String?,

)

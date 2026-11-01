package com.example.analytics.analytics_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.analytics.data.di.analyticsDataModule
import com.example.analytics.presentation.AnalyticsDashboardScreenRot
import com.example.analytics.presentation.di.analyticsPresentationModule
import com.example.core.presentation.designsystem.RunTheme
import com.google.android.play.core.splitcompat.SplitCompat
import org.koin.core.context.loadKoinModules

class AnalyticsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(listOf(analyticsDataModule,
            analyticsPresentationModule
        ))
        SplitCompat.installActivity(this)

        setContent {
            RunTheme {
                val navHostController = rememberNavController()
                NavHost(navController = navHostController, startDestination = "analytics_dashboard"
                    ) {
                    composable("analytics_dashboard"){
                        AnalyticsDashboardScreenRot(onBackClick = {finish()})
                    }
                }

            }
        }
    }
}
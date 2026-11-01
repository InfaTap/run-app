package com.example.analytics.presentation.di

import com.example.analytics.presentation.AnalyticsDashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val analyticsPresentationModule = module {

    viewModelOf(::AnalyticsDashboardViewModel)

}
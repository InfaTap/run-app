package com.example.run.presentation.di

import com.example.run.presentation.run_active.ActiveRunViewModel
import com.example.run.presentation.run_overview.RunOverviewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val runViewModelModule = module {
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}

package com.example.auth.presentation.di

import com.example.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authViewModelModule = module {
    singleOf(::RegisterViewModel)
}
package com.example.auth.presentation.di

import com.example.auth.presentation.login.LoginViewModel
import com.example.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}

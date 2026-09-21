package com.example.data.di

import com.example.auth.domain.AuthRepository
import com.example.auth.domain.PatternValidator
import com.example.auth.domain.UserDataValidator
import com.example.data.AuthRepositoryImpl
import com.example.data.EmailValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}
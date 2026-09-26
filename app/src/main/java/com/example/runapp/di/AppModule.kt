package com.example.runapp.di

import android.content.SharedPreferences
import android.content.Context
import com.example.auth.domain.PatternValidator
import com.example.auth.domain.UserDataValidator
import com.example.data.EmailValidator
import com.example.runapp.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<SharedPreferences>{
        // Values are encrypted with an Android Keystore key in EncryptedSessionStorage
        androidContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    viewModelOf(::MainViewModel)
}
package com.example.runapp

data class MainState(
   val isCheckingAuth: Boolean = false,
   val isLoggedIn: Boolean = false,
   val showAnalyticsInstallDialog: Boolean = false

)

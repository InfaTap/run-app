package com.example.runapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.auth.presentation.intro.IntroScreenRoot
import com.example.auth.presentation.login.LoginScreenRoot
import com.example.auth.presentation.register.RegisterScreenRoot
import com.example.run.presentation.run_active.ActiveRunScreenRoot
import com.example.run.presentation.run_active.service.ActiveRunService
import com.example.run.presentation.run_overview.RunOverViewScreenRoot

@Composable
fun NavigationRoot(
    navHostController: NavHostController,
    isLoggedIn: Boolean = false
) {

    NavHost(
        navController = navHostController,
        startDestination = if (isLoggedIn) "run" else "auth"
    ) {
        authGraph(navHostController)
        runGraph(navHostController)
    }
}

private fun NavGraphBuilder.authGraph(
    navHostController: NavHostController
) {
    navigation(
        startDestination = "intro",
        route = "auth"
    ) {
        composable(route = "intro") {
            IntroScreenRoot(
                onSignInClick = {
                    navHostController.navigate("login")
                },
                onSignUpClick = {
                    navHostController.navigate("register")
                })
        }
        composable(route = "register") {
            RegisterScreenRoot(
                onSignInClick = {
                    navHostController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navHostController.navigate("login")
                }
            )
        }
        composable(route = "login") {

            LoginScreenRoot(

                onSignUpClick = {
                    navHostController.navigate("register") {
                        popUpTo("login") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onLoginSuccess = {
                    navHostController.navigate("run") {
                        popUpTo("auth") {
                            inclusive = true

                        }

                    }
                },

                )
        }
    }
}

private fun NavGraphBuilder.runGraph(
    navHostController: NavHostController
) {
    navigation(
        startDestination = "run_overview",
        route = "run",
    ) {
        composable("run_overview") {
            RunOverViewScreenRoot(
                onRunClick = {
                    navHostController.navigate("active_run")
                }
            )
        }
        composable(
            route = "active_run",
            deepLinks = listOf(navDeepLink { uriPattern = "runapp://active_run" })
        ) {
            val context = LocalContext.current
            ActiveRunScreenRoot(
                onBack = {
                    navHostController.navigateUp()
                },
                onFinish = {
                    navHostController.navigateUp()
                },
                onServiceToggle = { shouldServiceRun ->
                    if (shouldServiceRun) {
                        context.startService(
                            ActiveRunService.createStartIntent(
                                context = context,
                                activityClass = MainActivity::class.java
                            )
                        )
                    } else {
                        context.stopService(
                            ActiveRunService.createStopIntent(
                                context = context
                            )
                        )
                    }
                }
            )
        }
    }
}
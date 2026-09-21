package com.example.runapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.auth.presentation.intro.IntroScreenRoot
import com.example.auth.presentation.register.RegisterScreenRoot

@Composable
fun NavigationRoot(
    navHostController: NavHostController
) {

    NavHost(
        navController = navHostController,
        startDestination = "auth"
    ) {
        authGraph(navHostController)
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
                    navHostController.navigate("login"){
                        popUpTo("register"){
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
    }
}
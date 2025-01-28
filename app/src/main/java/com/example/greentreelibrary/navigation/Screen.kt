package com.example.greentreelibrary.navigation

sealed class Screen(val route: String) {
    object SignIn : Screen("signin")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
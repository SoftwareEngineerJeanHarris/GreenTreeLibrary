package com.example.greentreelibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greentreelibrary.navigation.ProfileScreen
import com.example.greentreelibrary.navigation.Screen
import com.example.greentreelibrary.navigation.SettingsScreen
import com.example.greentreelibrary.screens.*
import com.example.greentreelibrary.ui.theme.GreenTreeLibraryTheme
import com.example.greentreelibrary.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreenTreeLibraryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationApp()
                }
            }
        }
    }
}

@Composable
fun NavigationApp(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Only show the drawer if the user is logged in (i.e., not on the SignIn screen)
    if (currentScreen != Screen.SignIn) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.width(240.dp)) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "GreenTreeLibrary",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Home") },
                        selected = currentScreen == Screen.Home,
                        onClick = {
                            viewModel.navigateTo(Screen.Home)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                    NavigationDrawerItem(
                        label = { Text("Profile") },
                        selected = currentScreen == Screen.Profile,
                        onClick = {
                            viewModel.navigateTo(Screen.Profile)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = currentScreen == Screen.Settings,
                        onClick = {
                            viewModel.navigateTo(Screen.Settings)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        ) {
            AppContent(navController, currentScreen, viewModel, drawerState, scope)
        }
    } else {
        AppContent(navController, currentScreen, viewModel, drawerState, scope)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(
    navController: NavHostController,
    currentScreen: Screen,
    viewModel: MainViewModel,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Scaffold(
        topBar = {
            if (currentScreen != Screen.SignIn) {
                TopAppBar(
                    title = { Text(currentScreen.route) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController, startDestination = Screen.SignIn.route) {
                composable(Screen.SignIn.route) { SignInScreen(viewModel) }
                composable(Screen.Home.route) { HomeScreen() }
                composable(Screen.Profile.route) { ProfileScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        }
    }

    // Navigate to the current screen
    LaunchedEffect(currentScreen) {
        navController.navigate(currentScreen.route) {
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
    }
}
package com.example.greentreelibrary.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.greentreelibrary.navigation.Screen

class MainViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow<Screen>(Screen.SignIn)
    val currentScreen: StateFlow<Screen> get() = _currentScreen

    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _currentScreen.value = screen
        }
    }
}
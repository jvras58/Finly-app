package com.equipealpha.financaspessoais

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.equipealpha.financaspessoais.data.repository.AuthRepository
import com.equipealpha.financaspessoais.navigation.AppNavigation
import com.equipealpha.financaspessoais.navigation.Routes
import com.equipealpha.financaspessoais.ui.LocalActivity
import com.equipealpha.financaspessoais.ui.theme.FinancasPessoaisTheme
import com.equipealpha.financaspessoais.viewmodel.AuthViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate chamado, savedInstanceState: $savedInstanceState")
        val authRepository = AuthRepository(applicationContext)
        val authViewModel = ViewModelProvider(
            this,
            AuthViewModel.provideFactory(authRepository)
        )[AuthViewModel::class.java]

        setContent {
            CompositionLocalProvider(LocalActivity provides this) {
                var isDarkTheme by rememberSaveable { mutableStateOf(false) }
                FinancasPessoaisTheme(darkTheme = isDarkTheme) {
                    val navController = rememberNavController()
                    val userState by authViewModel.userState.collectAsStateWithLifecycle()

                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        startDestination = if (userState == null) Routes.LOGIN else Routes.MAIN_APP,
                        onToggleTheme = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}
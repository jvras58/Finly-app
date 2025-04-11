package com.equipealpha.financaspessoais

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.equipealpha.financaspessoais.data.repository.AuthRepository
import com.equipealpha.financaspessoais.ui.root.HomeRootScreen
import com.equipealpha.financaspessoais.ui.theme.FinancasPessoaisTheme
import com.equipealpha.financaspessoais.viewmodel.AuthViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.compose.runtime.staticCompositionLocalOf
import com.equipealpha.financaspessoais.navigation.AppNavigation
import com.equipealpha.financaspessoais.navigation.Routes
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Add this import
import com.equipealpha.financaspessoais.ui.LocalActivity


class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authRepository = AuthRepository(applicationContext)
        authViewModel = ViewModelProvider(
            this,
            AuthViewModel.provideFactory(authRepository)
        )[AuthViewModel::class.java]

        setContent {
            CompositionLocalProvider(
                LocalActivity provides this
            ) {
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
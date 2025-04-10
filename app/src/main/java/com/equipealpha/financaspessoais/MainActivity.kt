package com.equipealpha.financaspessoais

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.equipealpha.financaspessoais.data.repository.AuthRepository
import com.equipealpha.financaspessoais.navigation.AppNavigation
import com.equipealpha.financaspessoais.navigation.Routes
import com.equipealpha.financaspessoais.ui.theme.FinancasPessoaisTheme
import com.equipealpha.financaspessoais.viewmodel.AuthViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.equipealpha.financaspessoais.ui.LocalActivity


class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crie a instância do AuthRepository usando o contexto da aplicação.
        val authRepository = AuthRepository(applicationContext)
        // Instancie o AuthViewModel utilizando o ViewModelProvider e a factory.
        authViewModel = ViewModelProvider(
            this,
            AuthViewModel.provideFactory(authRepository)
        ).get(AuthViewModel::class.java)

        setContent {
            // Obtenha o estado atual do usuário.
            val user by authViewModel.userState.collectAsState()
            // Define a rota inicial baseada no usuário autenticado.
            val startDestination = if (user != null) Routes.MAIN_APP else Routes.LOGIN

            // Fornece a Activity atual aos composables via CompositionLocal
            CompositionLocalProvider(LocalActivity provides this) {
                var isDarkTheme by rememberSaveable { mutableStateOf(false) }
                FinancasPessoaisTheme(darkTheme = isDarkTheme) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        startDestination = startDestination,  // Rota condicional
                        onToggleTheme = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}

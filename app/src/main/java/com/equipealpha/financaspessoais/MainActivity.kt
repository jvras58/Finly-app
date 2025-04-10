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

// Crie um CompositionLocal para a Activity, caso precise utilizar em outras telas
val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("Nenhuma Activity foi fornecida")
}

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
            // Aqui, além de fornecer o LocalActivity, vamos fornecer explicitamente o LocalViewModelStoreOwner.
            CompositionLocalProvider(
                LocalActivity provides this,
                androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner provides this as ViewModelStoreOwner
            ) {
                var isDarkTheme by rememberSaveable { mutableStateOf(false) }
                FinancasPessoaisTheme(darkTheme = isDarkTheme) {
                    val navController = rememberNavController()
                    HomeRootScreen(
                        navController = navController,
                        onToggleTheme = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}

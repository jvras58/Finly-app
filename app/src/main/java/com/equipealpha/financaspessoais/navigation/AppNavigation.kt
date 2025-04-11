package com.equipealpha.financaspessoais.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.equipealpha.financaspessoais.ui.login.LoginScreen
import com.equipealpha.financaspessoais.ui.root.HomeRootScreen
import com.equipealpha.financaspessoais.viewmodel.AuthViewModel

object Routes {
    const val EDIT_TRANSACTION = "edit_transaction"

    // Rotas públicas
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Rota que carrega o app com scaffold completo
    const val MAIN_APP = "main_app"

    // Rotas privadas (usadas dentro do HomeRootScreen)
    const val HOME = "home"
    const val ADD_MONEY = "add_money"
    const val WITHDRAW_MONEY = "withdraw_money"
    const val SETTINGS = "settings"
    const val TRANSACTIONS = "transactions"
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    startDestination: String,
    modifier: Modifier = Modifier,
    onToggleTheme: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Routes.MAIN_APP) {
            HomeRootScreen(
                navController = navController,
                onToggleTheme = onToggleTheme
            )
        }
    }
}

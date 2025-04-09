package com.equipealpha.financaspessoais.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.equipealpha.financaspessoais.ui.cadastro.RegisterScreen
import com.equipealpha.financaspessoais.ui.login.LoginScreen
import com.equipealpha.financaspessoais.ui.root.HomeRootScreen

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
    const val EDIT_PROFILE = "edit_profile"



}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onToggleTheme: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        // Login e cadastro
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController)
        }

        // Rota com scaffold, FAB, BottomNav e navegação interna
        composable(Routes.MAIN_APP) {
            HomeRootScreen(
                navController = navController,
                onToggleTheme = onToggleTheme
            )
        }
    }
}

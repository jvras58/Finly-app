package com.equipealpha.financaspessoais.ui.root

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.equipealpha.financaspessoais.navigation.Routes
import com.equipealpha.financaspessoais.ui.dashboard.HomeScreen
import com.equipealpha.financaspessoais.ui.navigation.BottomNavigationBar
import com.equipealpha.financaspessoais.ui.settings.SettingsScreen
import com.equipealpha.financaspessoais.ui.transacao.AddMoneyScreen
import com.equipealpha.financaspessoais.ui.transacao.EditTransactionScreen
import com.equipealpha.financaspessoais.ui.transacao.TransactionListScreen
import com.equipealpha.financaspessoais.ui.transacao.WithdrawMoneyScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRootScreen(
    navController: NavHostController,
    onToggleTheme: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Nova ação")
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(navController)
            }

            composable(Routes.ADD_MONEY) {
                AddMoneyScreen(navController)
            }

            composable(Routes.WITHDRAW_MONEY) {
                WithdrawMoneyScreen(navController)
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    navController = navController,
                    onToggleTheme = onToggleTheme,
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.TRANSACTIONS) { TransactionListScreen(navController) }

            composable("${Routes.EDIT_TRANSACTION}/{id}") { backStackEntry ->
                EditTransactionScreen(navController, backStackEntry)
            }

        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            ListItem(
                headlineContent = { Text("Adicionar Entrada") },
                supportingContent = { Text("Ex: salário, presente...") },
                modifier = Modifier.clickable {
                    navController.navigate(Routes.ADD_MONEY)
                    showSheet = false
                }
            )
            ListItem(
                headlineContent = { Text("Registrar Saída") },
                supportingContent = { Text("Ex: lanche, transporte...") },
                modifier = Modifier.clickable {
                    navController.navigate(Routes.WITHDRAW_MONEY)
                    showSheet = false
                }
            )
        }
    }
}

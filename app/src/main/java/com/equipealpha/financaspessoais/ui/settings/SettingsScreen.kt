package com.equipealpha.financaspessoais.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.equipealpha.financaspessoais.navigation.Routes
import com.equipealpha.financaspessoais.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onToggleTheme: () -> Unit
) {
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                authViewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Configurações", style = MaterialTheme.typography.headlineMedium)

            Button(onClick = { onToggleTheme() }) {
                Text("Alternar Tema")
            }

            Button(onClick = {
                authViewModel.signOut()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }) {
                Text("Sair")
            }
        }
    }
}
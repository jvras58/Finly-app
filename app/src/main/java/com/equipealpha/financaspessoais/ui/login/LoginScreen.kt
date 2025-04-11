package com.equipealpha.financaspessoais.ui.login

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.equipealpha.financaspessoais.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel // Recebe o ViewModel como parâmetro
) {
    val context = LocalContext.current
    val activity = LocalContext.current as ComponentActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.signInWithGoogle(activity)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login com Google")
        }
    }

    // Observa o estado do usuário e navega para a tela principal se autenticado.
    val user by authViewModel.userState.collectAsState()
    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("main_app") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}

package com.equipealpha.financaspessoais.ui.login

import androidx.activity.compose.LocalActivity
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
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

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

    val user by authViewModel.userState.collectAsState()
    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("main_app") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}

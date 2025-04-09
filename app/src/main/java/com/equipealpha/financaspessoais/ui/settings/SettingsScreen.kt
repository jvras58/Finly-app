package com.equipealpha.financaspessoais.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.equipealpha.financaspessoais.navigation.Routes

@Composable
fun SettingsScreen(
    navController: NavController,
    onToggleTheme: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Configurações", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = {
            navController.navigate(Routes.EDIT_PROFILE)
        }) {
            Text("Editar Perfil")
        }


        Button(onClick = {
            onToggleTheme()
        }) {
            Text("Alternar Tema")
        }

        Button(onClick = { onLogout() }) {
            Text("Sair")
        }

    }
}

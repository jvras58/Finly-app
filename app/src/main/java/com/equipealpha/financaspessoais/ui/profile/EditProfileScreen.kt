package com.equipealpha.financaspessoais.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.equipealpha.financaspessoais.R
import com.equipealpha.financaspessoais.data.entities.UserProfile
import com.equipealpha.financaspessoais.viewmodel.UserProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: UserProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val perfil by viewModel.perfil.collectAsState()

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf("") }

    LaunchedEffect(perfil) {
        nome = perfil.nome
        email = perfil.email
        senha = perfil.senha
        avatarUri = perfil.avatarUri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.avatar_placeholder),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable {
                    // TODO: pode abrir um seletor de imagem futuramente
                    Toast
                        .makeText(context, "Alterar avatar (simulado)", Toast.LENGTH_SHORT)
                        .show()
                }
        )

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                val perfilAtualizado = UserProfile(
                    id = 1,
                    nome = nome,
                    email = email,
                    senha = senha,
                    avatarUri = avatarUri
                )
                viewModel.atualizarPerfil(perfilAtualizado)
                Toast.makeText(context, "Perfil atualizado", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar alterações")
        }
    }
}

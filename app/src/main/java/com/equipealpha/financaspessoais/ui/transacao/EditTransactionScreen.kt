package com.equipealpha.financaspessoais.ui.transacao

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry
import com.equipealpha.financaspessoais.data.entities.Transaction
import com.equipealpha.financaspessoais.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditTransactionScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    vm: TransactionViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }

    val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
    var transaction by remember { mutableStateOf<Transaction?>(null) }

    var valor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var dataSelecionada by remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(id) {
        id?.let {
            transaction = vm.buscarPorId(it)
            transaction?.let {
                valor = it.amount.toString()
                categoria = it.category
                descricao = it.description ?: ""
                dataSelecionada.timeInMillis = it.date
            }
        }
    }

    if (transaction == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Carregando transação...")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Editar Transação", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = sdf.format(dataSelecionada.time),
                onValueChange = {},
                label = { Text("Data") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                val valorDouble = valor.toDoubleOrNull()
                if (valorDouble != null) {
                    val updated = transaction!!.copy(
                        amount = valorDouble,
                        category = categoria,
                        description = descricao,
                        date = dataSelecionada.timeInMillis
                    )
                    vm.editarTransacao(updated)
                    Toast.makeText(context, "Transação atualizada", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Valor inválido", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Atualizar")
            }
        }
    }
}

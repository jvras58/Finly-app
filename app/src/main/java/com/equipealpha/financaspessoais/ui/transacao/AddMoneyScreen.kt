package com.equipealpha.financaspessoais.ui.transacao

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.equipealpha.financaspessoais.data.entities.Transaction
import com.equipealpha.financaspessoais.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoneyScreen(navController: NavController, vm: TransactionViewModel = viewModel()) {
    val context = LocalContext.current

    var valor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Salário") }
    var descricao by remember { mutableStateOf("") }
    var dataSelecionada by remember { mutableStateOf(Calendar.getInstance()) }

    val sdf = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Adicionar Dinheiro", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = valor,
            onValueChange = { valor = it },
            label = { Text("Valor") },
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown de categoria simples
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = {}
        ) {
            OutlinedTextField(
                value = categoria,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoria") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = sdf.format(dataSelecionada.time),
            onValueChange = {},
            label = { Text("Data") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            dataSelecionada.set(year, month, day)
                        },
                        dataSelecionada.get(Calendar.YEAR),
                        dataSelecionada.get(Calendar.MONTH),
                        dataSelecionada.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
        )

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Observações (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val valorDouble = valor.toDoubleOrNull()
            if (valorDouble != null) {
                val transacao = Transaction(
                    amount = valorDouble,
                    category = categoria,
                    date = dataSelecionada.timeInMillis,
                    type = "entrada",
                    description = descricao.ifBlank { null }
                )
                vm.adicionarTransacao(transacao)
                Toast.makeText(context, "Entrada registrada!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            } else {
                Toast.makeText(context, "Informe um valor válido", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Salvar")
        }
    }
}

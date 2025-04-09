package com.equipealpha.financaspessoais.ui.transacao

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.equipealpha.financaspessoais.data.entities.Transaction
import com.equipealpha.financaspessoais.navigation.Routes
import com.equipealpha.financaspessoais.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    vm: TransactionViewModel = viewModel()
) {
    val transacoes by vm.todasTransacoes.collectAsState()
    val sdf = remember { SimpleDateFormat("dd MMM yyyy", Locale("pt", "BR")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Histórico de Transações", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (transacoes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nenhuma transação registrada ainda.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(transacoes, key = { it.id }) { transacao ->
                    var showDialog by remember { mutableStateOf(false) }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    vm.deletarTransacao(transacao)
                                    showDialog = false
                                }) {
                                    Text("Excluir")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text("Cancelar")
                                }
                            },
                            title = { Text("Confirmar exclusão") },
                            text = { Text("Deseja realmente excluir esta transação?") }
                        )
                    }

                    val density = LocalDensity.current
                    val thresholdPx = with(density) { 150.dp.toPx() }

                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                showDialog = true
                            }
                            false
                        },
                        positionalThreshold = { thresholdPx }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Text("Excluir", color = Color.White)
                                }
                            }
                        },
                        enableDismissFromEndToStart = true,
                        enableDismissFromStartToEnd = false
                    ) {
                        TransactionCard(
                            transacao = transacao,
                            sdf = sdf,
                            onClick = {
                                navController.navigate("${Routes.EDIT_TRANSACTION}/${transacao.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionCard(
    transacao: Transaction,
    sdf: SimpleDateFormat,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        ListItem(
            headlineContent = { Text(transacao.category) },
            supportingContent = {
                Text(transacao.description ?: "Sem descrição")
            },
            leadingContent = {
                Icon(
                    imageVector = if (transacao.type == "entrada")
                        Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = if (transacao.type == "entrada") Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            },
            trailingContent = {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "R$ %.2f".format(transacao.amount),
                        color = if (transacao.type == "entrada") Color(0xFF2E7D32) else Color(0xFFC62828),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(sdf.format(Date(transacao.date)))
                }
            }
        )
    }
}

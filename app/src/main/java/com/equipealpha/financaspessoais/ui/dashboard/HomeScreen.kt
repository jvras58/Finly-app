package com.equipealpha.financaspessoais.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.equipealpha.financaspessoais.navigation.Routes
import com.equipealpha.financaspessoais.viewmodel.TransactionViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, vm: TransactionViewModel = viewModel()) {
    val totalEntradas by vm.totalEntradas.collectAsState()
    val totalSaidas by vm.totalSaidas.collectAsState()
    val saldo by vm.saldoAtual.collectAsState()

    val moedaBR = remember { NumberFormat.getCurrencyInstance(Locale("pt", "BR")) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumo Financeiro") },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardSaldo("Saldo Atual", moedaBR.format(saldo), MaterialTheme.colorScheme.primaryContainer)
            CardSaldo("Total de Entradas", moedaBR.format(totalEntradas), MaterialTheme.colorScheme.tertiaryContainer)
            CardSaldo("Total de Saídas", moedaBR.format(totalSaidas), MaterialTheme.colorScheme.errorContainer)
        }
    }
}

@Composable
fun CardSaldo(
    titulo: String,
    valor: String,
    cor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        colors = CardDefaults.cardColors(containerColor = cor),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = titulo, style = MaterialTheme.typography.labelLarge)
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

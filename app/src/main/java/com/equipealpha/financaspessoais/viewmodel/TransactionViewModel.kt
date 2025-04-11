package com.equipealpha.financaspessoais.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.equipealpha.financaspessoais.data.entities.Transaction
import com.equipealpha.financaspessoais.data.local.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).transactionDao()

    private val _totalEntradas = MutableStateFlow(0.0)
    val totalEntradas: StateFlow<Double> = _totalEntradas

    private val _todasTransacoes = MutableStateFlow<List<Transaction>>(emptyList())
    val todasTransacoes: StateFlow<List<Transaction>> = _todasTransacoes

    private val _totalSaidas = MutableStateFlow(0.0)
    val totalSaidas: StateFlow<Double> = _totalSaidas

    val saldoAtual: StateFlow<Double> = combine(
        totalEntradas,
        totalSaidas
    ) { entradas, saidas ->
        entradas - saidas
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    init {
        viewModelScope.launch {
            dao.getTotalEntradasFlow()
                .collect { _totalEntradas.value = it ?: 0.0 }
        }

        viewModelScope.launch {
            dao.getTotalSaidasFlow()
                .collect { _totalSaidas.value = it ?: 0.0 }
        }

        viewModelScope.launch {
            dao.getAllTransactionsFlow().collect { transacoes ->
                _todasTransacoes.value = transacoes
            }
        }
    }

    fun adicionarTransacao(transacao: Transaction) {
        viewModelScope.launch {
            dao.insert(transacao)
        }
    }
    fun deletarTransacao(transacao: Transaction) {
        viewModelScope.launch {
            dao.delete(transacao)
        }
    }

    suspend fun buscarPorId(id: Int): Transaction? {
        return dao.getById(id)
    }

    fun editarTransacao(transacao: Transaction) {
        viewModelScope.launch {
            dao.update(transacao)
        }
    }



}



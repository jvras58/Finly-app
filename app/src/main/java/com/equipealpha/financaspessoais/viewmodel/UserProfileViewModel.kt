package com.equipealpha.financaspessoais.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.equipealpha.financaspessoais.data.entities.UserProfile
import com.equipealpha.financaspessoais.data.local.AppDatabase
import com.equipealpha.financaspessoais.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).userProfileDao()
    private val repository = UserProfileRepository(dao)

    private val _perfil = MutableStateFlow(UserProfile())
    val perfil: StateFlow<UserProfile> = _perfil

    init {
        viewModelScope.launch {
            repository.getPerfil().collectLatest {
                _perfil.value = it!!
            }
        }
    }

    fun salvarPerfil(profile: UserProfile) {
        viewModelScope.launch {
            repository.salvar(profile)
        }
    }

    fun atualizarPerfil(profile: UserProfile) {
        viewModelScope.launch {
            repository.atualizar(profile)
        }
    }

    suspend fun carregarPerfilUnico(): UserProfile? {
        return repository.carregarPerfilUnico()
    }
}

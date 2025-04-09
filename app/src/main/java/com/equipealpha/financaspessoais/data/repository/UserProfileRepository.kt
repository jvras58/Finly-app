package com.equipealpha.financaspessoais.data.repository

import com.equipealpha.financaspessoais.data.dao.UserProfileDao
import com.equipealpha.financaspessoais.data.entities.UserProfile
import kotlinx.coroutines.flow.Flow

class UserProfileRepository(private val dao: UserProfileDao) {

    fun getPerfil(): Flow<UserProfile?> = dao.getPerfil()

    suspend fun salvar(profile: UserProfile) {
        dao.salvarPerfil(profile)
    }

    suspend fun atualizar(profile: UserProfile) {
        dao.atualizarPerfil(profile)
    }

    suspend fun carregarPerfilUnico(): UserProfile? {
        return dao.getPerfilOnce()
    }
}

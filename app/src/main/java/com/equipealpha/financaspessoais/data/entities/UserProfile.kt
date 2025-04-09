package com.equipealpha.financaspessoais.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // perfil único
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val avatarUri: String = ""
)

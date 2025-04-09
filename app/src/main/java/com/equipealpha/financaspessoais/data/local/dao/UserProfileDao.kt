package com.equipealpha.financaspessoais.data.dao

import androidx.room.*
import com.equipealpha.financaspessoais.data.entities.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    // TODO: add FLOW couretine
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getPerfil(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarPerfil(profile: UserProfile)


    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getPerfilOnce(): UserProfile?

    @Update
    suspend fun atualizarPerfil(profile: UserProfile)

}

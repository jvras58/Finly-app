package com.equipealpha.financaspessoais.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser: Flow<FirebaseUser?> = _currentUser

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    suspend fun signInWithGoogleIdToken(idToken: String): Result<FirebaseUser?> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro no login com Google: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            Result.success(Unit)
        } catch (e: ClearCredentialException) {
            Log.e("AuthRepository", "Erro ao limpar credenciais: ${e.message}", e)
            Result.failure(e)
        }
    }
}
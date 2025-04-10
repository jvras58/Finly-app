package com.equipealpha.financaspessoais.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(private val context: Context) {
    // Instância singleton do FirebaseAuth.
    private val auth: FirebaseAuth = Firebase.auth

    // Flow para emitir atualizações de usuário autenticado.
    val currentUserFlow = callbackFlow<FirebaseUser?> {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    // Função para fazer o login com o token de autenticação do Google.
    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val result = auth.signInWithCredential(credential).await()
            result.user  // Retorna o usuário autenticado.
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao autenticar com Google: ${e.localizedMessage}")
            null
        }
    }

    // Função para deslogar e limpar o estado do Credential Manager.
    suspend fun signOut() {
        auth.signOut()
        val credentialManager = CredentialManager.create(context)
        try {
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        } catch (e: ClearCredentialException) {
            Log.e("AuthRepository", "Erro ao limpar credenciais: ${e.localizedMessage}")
        }
    }
}

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
    // Instância do FirebaseAuth (inicializada no companion object para garantir uma única instância)
    private val auth: FirebaseAuth = Firebase.auth

    // Flow que emite o usuário Firebase atual (null se deslogado)
    //  - Emite o FirebaseAuth e não o FirebaseUser para ter acesso à instância do FirebaseAuth no Flow, se necessário.
    val currentUserFlow = callbackFlow<FirebaseUser?> {
        // Listener para mudanças de estado de login no Firebase
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            // Emite o usuário atual (pode ser null)
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)

        // Encerrar o Flow e remover listener quando não for mais necessário
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser? {
        // Cria credencial Firebase a partir do ID Token do Google
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            // Faz login no Firebase de forma assíncrona (usando await() da KTX)
            val result = auth.signInWithCredential(credential).await()
            result.user  // FirebaseUser logado (ou null se falhou)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao fazer login com Google: ${e.localizedMessage}")
            null // Retorna null em caso de erro
        }
    }

    suspend fun signOut() {
        // Faz logout no Firebase
        auth.signOut()
        // Limpa o estado de credenciais Google armazenadas (Credential Manager)
        val credentialManager = CredentialManager.create(context)
        try {
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        } catch (e: ClearCredentialException) {
            Log.e("AuthRepository", "Erro ao limpar credenciais: ${e.localizedMessage}")
        }
    }
}
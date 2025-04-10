package com.equipealpha.financaspessoais.viewmodel

import com.equipealpha.financaspessoais.R

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.credentials.*
import com.equipealpha.financaspessoais.data.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class AuthViewModel(private val authRepo: AuthRepository) : ViewModel() {

    // StateFlow contendo o usuário atual (null se não autenticado)
    private val _userState = MutableStateFlow<FirebaseUser?>(null)
    val userState: StateFlow<FirebaseUser?> = _userState

    init {
        // Coleta o fluxo de usuário do AuthRepository e atualiza o StateFlow
        viewModelScope.launch {
            authRepo.currentUserFlow.collect { user ->
                _userState.value = user
            }
        }
    }

    fun signInWithGoogle(activity: ComponentActivity) {
        // Inicia o fluxo de login do Google utilizando o Credential Manager
        viewModelScope.launch {
            try {
                // 1. Configura a opção de login Google com o ID do cliente (Web)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(activity.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(true)  // sugere contas já utilizadas
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                // 2. Chama o Credential Manager para obter a credencial (mostra UI Google se necessário)
                val credentialManager = CredentialManager.create(activity)
                val result = credentialManager.getCredential(activity, request)
                // 3. Verifica se a credencial retornada é um ID Token do Google
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Extrai o ID Token da credencial Google
                    val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleCredential.idToken
                    // 4. Usa o ID Token para autenticar no Firebase
                    authRepo.signInWithGoogleIdToken(idToken)
                    // (O listener do FirebaseAuth atualizará userState automaticamente)
                } else {
                    // Tipo de credencial inesperado
                    Log.w("AuthViewModel", "Credencial não é do tipo Google ID.")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Falha no login com Google: ${e.localizedMessage}")
            }
        }
    }

    fun signOut() {
        // Executa logout usando o repositório
        viewModelScope.launch {
            authRepo.signOut()
        }
    }
}

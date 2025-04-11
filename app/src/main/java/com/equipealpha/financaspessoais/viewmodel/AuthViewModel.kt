package com.equipealpha.financaspessoais.viewmodel

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.equipealpha.financaspessoais.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.credentials.*
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class AuthViewModel(private val authRepo: AuthRepository) : ViewModel() {

    // StateFlow que guarda o usuário autenticado (null se não estiver logado)
    private val _userState = MutableStateFlow<FirebaseUser?>(null)
    val userState: StateFlow<FirebaseUser?> = _userState

    init {
        viewModelScope.launch {
            authRepo.currentUserFlow.collect { user ->
                _userState.value = user
            }
        }
    }

    // Inicia o fluxo de login com Google.
    fun signInWithGoogle(activity: ComponentActivity) {
        viewModelScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(activity.getString(com.equipealpha.financaspessoais.R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false) // Ou remova o filtro
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val credentialManager = CredentialManager.create(activity)
                val result = credentialManager.getCredential(activity, request)
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleCredential.idToken
                    authRepo.signInWithGoogleIdToken(idToken)
                } else {
                    Log.w("AuthViewModel", "Credencial não é do tipo Google ID.")
                    // Aqui você pode informar ao usuário que o login não foi realizado
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Falha no login com Google: ${e.localizedMessage}")
                // Exiba uma mensagem para o usuário solicitando que verifique as contas Google ou adicione uma conta
            }
        }
    }


    // Efetua o logout.
    fun signOut() {
        viewModelScope.launch {
            authRepo.signOut()
        }
    }

    // Factory para instanciar o ViewModel via ViewModelProvider.
    companion object {
        fun provideFactory(authRepo: AuthRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                        return AuthViewModel(authRepo) as T
                    }
                    throw IllegalArgumentException("ViewModel desconhecido")
                }
            }
    }
}

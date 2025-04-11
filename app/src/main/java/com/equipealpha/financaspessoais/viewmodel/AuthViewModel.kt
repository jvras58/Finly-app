package com.equipealpha.financaspessoais.viewmodel

import android.app.Activity
import android.util.Log
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
    fun signInWithGoogle(activity: Activity?) {
        viewModelScope.launch {
            try {
                val googleIdOption = activity?.let {
                    GetGoogleIdOption.Builder()
                        .setServerClientId(it.getString(com.equipealpha.financaspessoais.R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                }

                val request = googleIdOption?.let {
                    GetCredentialRequest.Builder()
                        .addCredentialOption(it)
                        .build()
                }

                val credentialResponse = activity?.let {
                    if (request != null) {
                        CredentialManager.create(it).getCredential(it, request)
                    } else null
                }

                val credential = credentialResponse?.credential

                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleCredential.idToken

                    authRepo.signInWithGoogleIdToken(idToken)
                } else {
                    Log.w("AuthViewModel", "Credencial não é do tipo Google ID.")
                }

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Falha no login com Google: ${e.localizedMessage}")
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

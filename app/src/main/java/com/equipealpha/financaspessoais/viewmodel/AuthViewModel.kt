package com.equipealpha.financaspessoais.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.equipealpha.financaspessoais.R
import com.equipealpha.financaspessoais.data.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepo: AuthRepository) : ViewModel() {

    private val _userState = MutableStateFlow<FirebaseUser?>(null)
    val userState: StateFlow<FirebaseUser?> = _userState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            authRepo.currentUser.collect { user ->
                _userState.value = user
            }
        }
    }

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val credentialManager = CredentialManager.create(activity)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(activity.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(true)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                Log.d("AuthViewModel", "Iniciando getCredential")
                val result = credentialManager.getCredential(activity, request)
                val credential = result.credential

                Log.d("AuthViewModel", "Credencial recebida: type=${credential.type}, data=${credential.data}")

                // Verificar o tipo explicitamente
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleCredential.idToken
                    Log.d("AuthViewModel", "ID Token obtido: $idToken")
                    val authResult = authRepo.signInWithGoogleIdToken(idToken)
                    if (authResult.isSuccess) {
                        _userState.value = authResult.getOrNull()
                        Log.d("AuthViewModel", "Login bem-sucedido: ${_userState.value?.email}")
                    } else {
                        _errorMessage.value = "Falha na autenticação: ${authResult.exceptionOrNull()?.message}"
                        Log.e("AuthViewModel", "Falha na autenticação: ${authResult.exceptionOrNull()?.message}")
                    }
                } else {
                    _errorMessage.value = "Credencial inválida: tipo recebido=${credential.type}, esperado=${GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL}"
                    Log.e("AuthViewModel", "Credencial inválida: tipo recebido=${credential.type}, esperado=${GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL}")
                }
            } catch (e: GetCredentialException) {
                _errorMessage.value = "Erro ao obter credencial: ${e.message}"
                Log.e("AuthViewModel", "Erro ao obter credencial: ${e.message}", e)
            } catch (e: Exception) {
                _errorMessage.value = "Erro inesperado: ${e.message}"
                Log.e("AuthViewModel", "Erro inesperado: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val result = authRepo.signOut()
            if (result.isFailure) {
                _errorMessage.value = "Erro ao sair: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    companion object {
        fun provideFactory(authRepo: AuthRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                        return AuthViewModel(authRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
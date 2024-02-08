package com.example.gateguardianapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.domain.model.User
import com.example.gateguardianapp.domain.repository.UserRepository
import com.example.gateguardianapp.presentation.auth.googleclient.SignInResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    val repository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(
        result: SignInResult
    ) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return repository.getUserByEmail(email)
    }

    fun saveUser(name: String, email: String, category: String) {
        val userRequestBody = User(name, email, category).toRequestBody()
        viewModelScope.launch {
            repository.saveUser(userRequestBody)
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}
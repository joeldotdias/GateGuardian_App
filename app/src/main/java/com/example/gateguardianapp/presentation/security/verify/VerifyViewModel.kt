package com.example.gateguardianapp.presentation.security.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.domain.repository.SecurityApiRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val repository: SecurityApiRepository
): ViewModel() {

    private val _state = MutableStateFlow(VerifyState())
    val state = _state.asStateFlow()

    val email = Firebase.auth.currentUser?.email!!

    init {
        getVisitors()
    }

    fun getVisitors() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    visitors = repository.getVisitorsBySociety(email)
                )
            } catch(e: Exception) {
                _state.value = state.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    fun deleteVerifiedVisitor(visitorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteVerifiedVisitor(visitorId)
        }
    }
}
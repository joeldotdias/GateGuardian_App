package com.example.gateguardianapp.presentation.security.logs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.domain.repository.SecurityApiRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val repository: SecurityApiRepository
): ViewModel() {
    private val _state = MutableStateFlow(LogsState())
    val state = _state.asStateFlow()

    private val email = Firebase.auth.currentUser?.email!!

    init {
        getVisitorLogs()
    }

    fun getVisitorLogs() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    visitorLogs = repository.getVisitorLogs(email)
                )
                Log.d("Cloudfire", "getVisitorLogs: ${state.value.visitorLogs}")
            } catch(e: Exception) {
                _state.value = state.value.copy(
                    errorMessage = e.message
                )
                Log.d("Cloudfire", "getVisitorLogs: ${state.value.errorMessage}")
            }
        }
    }
}
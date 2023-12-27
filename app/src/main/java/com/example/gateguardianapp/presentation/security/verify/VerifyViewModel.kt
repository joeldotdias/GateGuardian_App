package com.example.gateguardianapp.presentation.security.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto
import com.example.gateguardianapp.domain.repository.SecurityApiRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val repository: SecurityApiRepository
): ViewModel() {

    private var _state = MutableStateFlow(VerifyState())
    val state = _state.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private val _visitors = MutableStateFlow<List<VisitorSecurityDto>?>(null)
    val visitors: StateFlow<List<VisitorSecurityDto>?>
        get() = _visitors.asStateFlow()

    val email = Firebase.auth.currentUser?.email!!

    init {
        getVisitors()
    }

    fun getVisitors() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableListOf<VisitorSecurityDto>()
            async(Dispatchers.IO) {
                _visitors.emit(repository.getVisitorsBySociety(email))
            }.await()
            _isRefreshing.emit(false)
        }
    }

    fun moveVerifiedVisitorToLogs(visitorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.moveVerifiedVisitorToLogs(visitorId)
        }
    }
}
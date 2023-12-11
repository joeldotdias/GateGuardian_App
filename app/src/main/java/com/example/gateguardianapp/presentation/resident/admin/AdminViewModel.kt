package com.example.gateguardianapp.presentation.resident.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.domain.repository.ResidentApiRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: ResidentApiRepository
): ViewModel() {

    private val adminEmail = Firebase.auth.currentUser?.email.toString()

    private val _state = MutableStateFlow(AdminScreenState())
    val state = _state.asStateFlow()

    init {
        getAdminScreenDetails()
    }

    fun getAdminScreenDetails() {
        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _state.value = state.value.copy(
//                    residents = repository.getResidentsBySociety(adminEmail)
//                )
//            } catch(e: Exception) {
//                _state.value = state.value.copy(
//                    errorMessage = e.message
//                )
//            }
            try {
                _state.value = state.value.copy(
                    residents = async {
                        repository.getResidentsBySociety(adminEmail)
                    }.await(),
                    securities = async {
                        repository.getSecuritiesBySociety(adminEmail)
                    }.await()
                )
            } catch(e: Exception) {
                _state.value = state.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    fun addResident(name: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveResident(name, email, adminEmail)
        }
    }
}
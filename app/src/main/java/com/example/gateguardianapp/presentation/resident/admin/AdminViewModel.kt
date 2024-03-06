package com.example.gateguardianapp.presentation.resident.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.domain.repository.ResidentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: ResidentRepository
): ViewModel() {

    private val _state = MutableStateFlow(AdminScreenState())
    val state = _state.asStateFlow()

    init {
        getAdminScreenDetails()
    }

    fun getAdminScreenDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    residents = async {
                        repository.getResidentsBySociety()
                    }.await(),
                    securities = async {
                        repository.getSecuritiesBySociety()
                    }.await(),
                    notices = async {
                        repository.getNotices()
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
            repository.saveResident(name, email)
        }
    }

    fun addSecurity(name: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveSecurity(name, email)
        }
    }

    fun addNotice(title: String, body: String, category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNotice(title, body, category)
        }
    }
}
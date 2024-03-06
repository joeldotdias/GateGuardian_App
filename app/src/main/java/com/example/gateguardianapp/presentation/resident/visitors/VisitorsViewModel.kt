package com.example.gateguardianapp.presentation.resident.visitors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.domain.repository.ResidentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisitorsViewModel @Inject constructor(
    private val repository: ResidentRepository
): ViewModel() {

    private val _state = MutableStateFlow(VisitorsState())
    val state = _state.asStateFlow()

    init {
        getVisitorsByResident()
    }

    fun getVisitorsByResident() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    visitors = repository.getVisitorsByResidentEmail()
                )
            } catch(e: Exception) {
                _state.value = state.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    suspend fun saveVisitor(name: String, phoneNo: String) {
        repository.saveVisitor(name, phoneNo)
    }

    suspend fun getRecentVisitorOtp(): String? {
        return repository.getRecentVisitorOtp()
    }
}
package com.example.gateguardianapp.presentation.resident.regulars

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
class RegularsViewModel @Inject constructor(
    private val repository: ResidentRepository
): ViewModel() {

    private val _state = MutableStateFlow(RegularsState())
    val state = _state.asStateFlow()

    init {
        getRegularsByResident()
    }

    fun getRegularsByResident() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    regulars = repository.getRegularsByResidentEmail()
                )
            } catch(e: Exception) {
                _state.value = state.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    suspend fun saveRegular(name: String, role: String, entry: String) {
        repository.saveRegular(name, role, entry)
    }

    suspend fun getRecentRegularOtp(): String? {
        return repository.getRecentRegularOtp()
    }

}
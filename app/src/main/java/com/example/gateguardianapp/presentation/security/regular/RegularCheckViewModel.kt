package com.example.gateguardianapp.presentation.security.regular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.domain.repository.SecurityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegularCheckViewModel @Inject constructor(
    private val repository: SecurityRepository
): ViewModel() {

    private val _state = MutableStateFlow(RegularState())
    val state = _state.asStateFlow()

    init {
        getRegularsByResident()
    }

    private fun getRegularsByResident() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    regulars = repository.getRegulars()
                )
            } catch(e: Exception) {
                _state.value = state.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }
}
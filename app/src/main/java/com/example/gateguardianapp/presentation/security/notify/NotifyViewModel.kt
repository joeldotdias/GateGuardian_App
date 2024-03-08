package com.example.gateguardianapp.presentation.security.notify

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.data.remote.dto.NotifyResidentsDto
import com.example.gateguardianapp.domain.repository.SecurityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotifyViewModel @Inject constructor(
    private val repository: SecurityRepository
): ViewModel() {

    private val _state = MutableStateFlow(NotifyState())
    val state = _state.asStateFlow()

    init {
        getNumbersToNotify("0", "")
    }

    fun getNumbersToNotify(flatStr: String, building: String) {
        val flatNo = if(flatStr.isDigitsOnly()) flatStr.trim().toInt() else 0

        viewModelScope.launch {
                try {
                _state.value = state.value.copy(
                    callables = repository.getResidentsToNotify(flatNo, building.trim())
                )
            } catch(e: Exception) {
                _state.value = state.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    fun clearCallables() {
        _state.value = state.value.copy(
            callables = null
        )
    }
}
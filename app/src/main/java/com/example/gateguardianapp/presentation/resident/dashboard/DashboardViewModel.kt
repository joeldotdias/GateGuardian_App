package com.example.gateguardianapp.presentation.resident.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.domain.repository.ResidentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: ResidentRepository
): ViewModel() {

    private val _state = MutableStateFlow(DashboardScreenState())
    val state = _state.asStateFlow()

    init {
        getDashDetails()
    }

    private fun getDashDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    dashDetails = async {
                        repository.getDashProfile()
                    }.await(),
                    visitors = async {
                        repository.getVisitorsByResidentEmail()
                    }.await(),
                    notices = async {
                        repository.getNotices()
                    }.await()
                )
            } catch(e: HttpException) {
                _state.value = state.value.copy(
                    errorMessage = e.message()
                )
            }
        }
    }
}
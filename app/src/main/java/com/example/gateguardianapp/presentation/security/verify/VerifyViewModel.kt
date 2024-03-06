package com.example.gateguardianapp.presentation.security.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.data.local.VisitorSearchEntity
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto
import com.example.gateguardianapp.domain.repository.SecurityRepository
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
    private val repository: SecurityRepository
): ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private val _visitors = MutableStateFlow<List<VisitorSecurityDto>?>(null)
    val visitors: StateFlow<List<VisitorSecurityDto>?>
        get() = _visitors.asStateFlow()

    val visitorSearchResults = MutableStateFlow<List<VisitorSearchEntity>>(emptyList())

    init {
        getVisitors()
        getVisitorSearchResults("")
    }

    fun getVisitors() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableListOf<VisitorSecurityDto>()
            async(Dispatchers.IO) {
                _visitors.emit(repository.getVisitorsBySociety())
            }.await()
            _isRefreshing.emit(false)
        }
    }

    fun getVisitorSearchResults(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getVisitorSearchResults(searchQuery)
                .collect { listOfVisitorSearchResults ->
                    visitorSearchResults.value = listOfVisitorSearchResults
                }
        }
    }

    fun moveVerifiedVisitorToLogs(visitorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.moveVerifiedVisitorToLogs(visitorId)
        }
    }
}
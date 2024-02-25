package com.example.gateguardianapp.presentation.resident.notices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.data.remote.dto.NoticeDto
import com.example.gateguardianapp.domain.repository.ResidentRepository
import com.example.gateguardianapp.presentation.resident.visitors.VisitorsState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticesViewModel @Inject constructor(
    private val repository: ResidentRepository
): ViewModel() {
    private val email = Firebase.auth.currentUser?.email.toString()

//    private val _state = MutableStateFlow(NoticesState())
//    val state = _state.asStateFlow()

    private val _notices = MutableStateFlow<List<NoticeDto>?>(null)
    val notices: StateFlow<List<NoticeDto>?>
        get() = _notices.asStateFlow()

    var err: String? = null

    init {
        getNoticesByResident()
    }

    fun getNoticesByResident() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _notices.emit(repository.getNotices(email))
            } catch(e: Exception) {
                err = e.message
            }
        }
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _state.value = state.value.copy(
//                    visitors = repository.getVisitorsByResidentEmail(email)
//                )
//            } catch(e: Exception) {
//                _state.value = state.value.copy(
//                    errorMessage = e.message
//                )
//            }
//        }
    }

}
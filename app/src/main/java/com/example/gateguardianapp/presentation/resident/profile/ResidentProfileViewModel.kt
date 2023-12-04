package com.example.gateguardianapp.presentation.resident.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.data.cloud.FirebaseCloudClient
import com.example.gateguardianapp.domain.repository.ResidentApiRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResidentProfileViewModel @Inject constructor(
    private val repository: ResidentApiRepository
): ViewModel(){

    private val _state = MutableStateFlow(ResidentProfileState())
    val state = _state.asStateFlow()

    val email = Firebase.auth.currentUser?.email

    init {
        getProfileDetails()
    }

    fun getProfileDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    resident = repository.getResidentByEmail(email!!)
                )
            } catch(e: Exception) {
                _state.value = state.value.copy(errorMessage = e.message)
            }
        }
    }

    fun uploadPfpToCloud(uri: Uri, name: String, context: Context) {
        val type = "pfp"
        val firebaseCloudClient = FirebaseCloudClient()
        firebaseCloudClient.uploadToCloud(uri, name, type, context)
    }

    fun updateResidentPfpUrl(imgUrl: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateResidentPfp(email.toString(), imgUrl.toString())
        }
    }

    fun updateResidentProfile(name: String, aboutMe: String, phoneNo: String) {
        viewModelScope.launch {
            repository.updateResidentProfile(email.toString(), name, aboutMe, phoneNo)
        }
    }
}
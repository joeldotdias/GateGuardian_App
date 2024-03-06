package com.example.gateguardianapp.presentation.resident.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.data.cloud.FirebaseCloudClient
import com.example.gateguardianapp.domain.repository.ResidentRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ResidentProfileViewModel @Inject constructor(
    private val repository: ResidentRepository
): ViewModel(){

    private val _state = MutableStateFlow(ResidentProfileState())
    val state = _state.asStateFlow()

    init {
        getProfileDetails()
    }

    fun getProfileDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    resident = async {
                        repository.getResidentByEmail()
                    }.await(),
//                    eventMemories = async {
//                        repository.getMemoriesByResident(email)
//                    }.await()
                )
            } catch(e: Exception) {
                _state.value = state.value.copy(errorMessage = e.message)
            }
        }
    }

    fun saveHomeDetails(flatNo: String, building: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveResidentHomeDetails(flatNo, building)
        }
    }

    fun uploadPfpToCloud(uri: Uri, name: String, context: Context) {
        FirebaseCloudClient.uploadToCloud(uri, name, "pfp", context)
    }

    fun updateResidentPfpUrl(imgUrl: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateResidentPfp(imgUrl.toString())
        }
    }

    fun updateResidentProfile(aboutMe: String, phoneNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateResidentProfile(aboutMe, phoneNo)
        }
    }
}
package com.example.gateguardianapp.presentation.security.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gateguardianapp.data.cloud.FirebaseCloudClient
import com.example.gateguardianapp.domain.repository.SecurityRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecurityProfileViewModel @Inject constructor(
    private val repository: SecurityRepository
): ViewModel() {

    private val _state = MutableStateFlow(SecurityProfileState())
    val state = _state.asStateFlow()

    val email = Firebase.auth.currentUser?.email!!

    init {
        getSecurityProfileDetails()
    }

    fun getSecurityProfileDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = state.value.copy(
                    security = repository.getSecurityByEmail()
                )
            } catch(e: Exception) {
                _state.value = state.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    fun uploadPfpToCloud(uri: Uri, name: String, context: Context) {
        FirebaseCloudClient.uploadToCloud(uri, name, "pfp", context)
    }

    fun updateSecurityPfpUrl(imgUrl: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSecurityPfp(imgUrl.toString())
        }
    }

    fun updateSecurityProfile(name: String, phoneNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSecurityProfile(name, phoneNo)
        }
    }
}
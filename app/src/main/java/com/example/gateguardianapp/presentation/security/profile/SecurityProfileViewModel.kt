package com.example.gateguardianapp.presentation.security.profile

//@HiltViewModel
//class SecurityProfileViewModel @Inject constructor(
//    private val repository: SecurityApiRepository
//): ViewModel() {
//
//    private val _state = MutableStateFlow(SecurityProfileState())
//    val state = _state.asStateFlow()
//
//    val email = Firebase.auth.currentUser?.email!!
//
//    fun getSecurityProfileDetails() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _state.value = state.value.copy(
//                    security = repository.getSecurityByEmail(email)
//                )
//            } catch(e: Exception) {
//                _state.value = state.value.copy(
//                    errorMessage = e.message
//                )
//            }
//        }
//    }
//
//    fun saveBadgeId(badgeId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            //
//        }
//    }
//
//    fun uploadPfpToCloud(uri: Uri, name: String, context: Context) {
//        FirebaseCloudClient.uploadToCloud(uri, name, "pfp", context)
//    }
//
//    fun updateResidentPfpUrl(imgUrl: Uri) {
//        viewModelScope.launch(Dispatchers.IO) {
//           // repository.updateResidentPfp(email, imgUrl.toString())
//        }
//    }
//
//    fun updateSecurityProfile(name: String, phoneNo: String) {
//        viewModelScope.launch {
//            //repository.updateResidentProfile(email, name, aboutMe, phoneNo)
//        }
//    }
//}
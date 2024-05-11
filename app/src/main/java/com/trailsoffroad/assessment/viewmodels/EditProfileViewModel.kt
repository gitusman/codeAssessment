package com.trailsoffroad.assessment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailsoffroad.assessment.models.UserProfile
import com.trailsoffroad.assessment.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val myRepository: MyRepository) : ViewModel() {

    val userProfile: StateFlow<UserProfile>
        get() = myRepository.profile

    suspend fun getProfile() {
        viewModelScope.launch {
            myRepository.getUserProfile()
        }
    }
    suspend fun setProfile(userProfile: UserProfile, onResponse:(String)->Unit) {
        myRepository.setUserProfile(userProfile){ response ->
            viewModelScope.launch {
                onResponse(response)
            }
        }
    }
}
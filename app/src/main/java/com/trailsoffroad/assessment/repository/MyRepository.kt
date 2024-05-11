package com.trailsoffroad.assessment.repository

import com.google.gson.Gson
import com.trailsoffroad.assessment.api.MyAPI
import com.trailsoffroad.assessment.models.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MyRepository @Inject constructor(private val myAPI: MyAPI) {

    private val _profile = MutableStateFlow(UserProfile.emptyProfile())
    val profile: StateFlow<UserProfile>
        get() = _profile

    suspend fun getUserProfile() {
        try {
            val result = myAPI.getUserProfile()
            if (result.isSuccessful && result.body() != null) {
                _profile.emit(result.body()!!)
            }
        } catch (e: Exception) {
            e.printStackTrace() // as there is no such host
            Thread.sleep(2000) // dummy wait
            _profile.emit(UserProfile("Usman Khan", "11/5/2000", "myemail@gmail.com", "+923334555555", true)) // for demo
        }
    }
    suspend fun setUserProfile(userProfile: UserProfile, onResponse:(String)->Unit) {
        try {
            val result = myAPI.updateProfile(Gson().toJson(userProfile))
            if (result.isSuccessful && result.body() != null) {
                onResponse(result.body()!!)
            }
        } catch (e: Exception) {
            e.printStackTrace() // as there is no such host
            Thread.sleep(2000) // dummy wait
            onResponse("Dummy Sent!") // for demo
        }
    }
}

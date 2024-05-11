package com.trailsoffroad.assessment.api

import com.trailsoffroad.assessment.models.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MyAPI {
    @POST("/updateProfile")
    @Headers("Content-Type: application/json")
    fun updateProfile(@Body userProfile: String): Response<String>

    @GET("/v3/getUserProfile")
    suspend fun getUserProfile(): Response<UserProfile>
}
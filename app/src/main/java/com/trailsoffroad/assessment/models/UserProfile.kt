package com.trailsoffroad.assessment.models

data class UserProfile(
    var fullName: String,
    var birthday: String,
    var email: String,
    var phone: String,
    var subscribeToNewsLetter: Boolean
//    var picture
) {
    companion object {
        fun emptyProfile(): UserProfile {
            return UserProfile("","","","",true)
        }
    }
}
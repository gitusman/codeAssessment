package com.trailsoffroad.assessment.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel

class MapScreenViewModel constructor(context: Context) : ViewModel() {
    companion object {
        private const val DEF_LAT = 31.054120
        private const val DEF_LNG = 70.201135
    }
}

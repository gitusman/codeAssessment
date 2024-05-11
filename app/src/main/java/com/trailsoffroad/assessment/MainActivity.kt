package com.trailsoffroad.assessment

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.trailsoffroad.assessment.screens.MainScreen
import com.trailsoffroad.assessment.ui.theme.CodingAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint
import org.maplibre.android.MapLibre
import org.maplibre.android.maps.MapView

@AndroidEntryPoint
class MainActivity : ComponentActivity(){
    private val PERMISSION_REQUEST_CODE = 4455
    private var mapView: MapView? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            CodingAssessmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainView(modifier = Modifier.padding(innerPadding), fusedLocationClient){
                        mapView = it
                    }
                }
            }
        }
        ActivityCompat.requestPermissions(this, arrayOf( ACCESS_FINE_LOCATION ), PERMISSION_REQUEST_CODE)

    }
}

@Composable fun MainView(modifier: Modifier = Modifier, locClient: FusedLocationProviderClient, onMapLoaded:(MapView)->Unit) {
    MainScreen(modifier, locClient, onMapLoaded)
}

@Preview(showBackground = true, showSystemUi = true) @Composable
fun MainPreview() {
    CodingAssessmentTheme {
        MainView(modifier = Modifier.fillMaxSize(), locClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)){ _->}
    }
}
package com.trailsoffroad.assessment.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.trailsoffroad.assessment.R
import org.maplibre.android.annotations.IconFactory
import org.maplibre.android.annotations.Marker
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

var mapView: MapView? = null
var marker: Marker? = null
@Composable
fun MapScreen(locClient: FusedLocationProviderClient, onMapLoaded:(MapView)->Unit) {

    val context = LocalContext.current
    if (mapView == null){
        mapView = MapView(context)//by rememberUpdatedState(newValue = MapView(LocalContext.current))
        mapView?.getMapAsync { map ->
            map.setStyle("https://demotiles.maplibre.org/style.json")
        }
        moveMapToMyLocation(locClient, context)
    }

    Box() {
        AndroidView(factory = { context ->
            mapView!!
        }) { customView ->
            onMapLoaded(customView)
        }
        SmallFloatingActionButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(10.dp), onClick = { moveMapToMyLocation(locClient, context)}) {
            Icon(imageVector = Icons.Filled.MyLocation, contentDescription = stringResource(R.string.take_to_my_location_button))
        }
    }
}

fun moveMapToMyLocation(locClient: FusedLocationProviderClient, context: Context) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return
    }
    mapView?.getMapAsync { map ->
        locClient.lastLocation
        .addOnSuccessListener { location : Location? ->
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                map.cameraPosition = CameraPosition.Builder().target(latLng).zoom(8.0).build()
                if (marker != null)
                    marker?.remove()
                val markerOptions = MarkerOptions()
                val icon = IconFactory.getInstance(context).fromResource(R.drawable.curr_loc)
                markerOptions.position(latLng).setIcon(icon)
                marker = map.addMarker(markerOptions)
            }
        }

    }

}


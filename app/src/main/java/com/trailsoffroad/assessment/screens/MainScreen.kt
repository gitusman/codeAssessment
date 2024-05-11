@file:OptIn(ExperimentalFoundationApi::class)

package com.trailsoffroad.assessment.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import org.maplibre.android.maps.MapView

data class TabItem(val name:String, val selectedImage: ImageVector, val unselectedImage: ImageVector)

val tabItems = listOf(
    TabItem("Map", Icons.Filled.Map, Icons.Outlined.Map),
    TabItem("Edit Profile", Icons.Filled.Person, Icons.Outlined.Person))

@Composable
fun MainScreen(modifier: Modifier, locClient: FusedLocationProviderClient, onMapLoaded:(MapView)->Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState {
        tabItems.size
    }
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(selectedIndex) {

    }
    Column(modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedIndex) {
            tabItems.forEachIndexed { index, tabItem ->
                Tab(selected = index == selectedIndex, onClick = {
                    selectedIndex = index
                    coroutine.launch {
                        pagerState.animateScrollToPage(selectedIndex)
                    }
                }, text = {
                    Text(text = tabItem.name)
                }, icon = {
                    Icon(imageVector = if (selectedIndex == index) tabItem.selectedImage
                    else tabItem.unselectedImage, contentDescription = "${tabItem.name} Tab")

                })
            }
        }
        HorizontalPager(state = pagerState, Modifier
            .fillMaxWidth()
            .weight(1f),
            userScrollEnabled = false) { index ->
            if (index == 0)
                MapScreen(locClient, onMapLoaded)
            else
                EditProfileScreen()
        }
    }

}
package com.example.guidemetravelersapp.views.audioguidemap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.views.homescreen.SearchView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AudioGuideMap : ComponentActivity() {
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeTravelersAppTheme {
                AudioGuideMapContent()
            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun AudioGuideMapContent() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = Modifier.fillMaxSize(),
        content = {
            SearchView(textState)
            MapScreen(
                50.937616532313434,
                6.960581381481977,
                "Cologne Cathedral",
                modifier = Modifier.height(300.dp))
            Text(
                text = "Available places with audio guide",
                modifier = Modifier.padding(15.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun MapScreen(latitude: Double, longitude: Double, title: String, modifier: Modifier) {
    val mapView = rememberMapViewWithLifecycle()
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val openDialog = remember { mutableStateOf(true) }

    // Track if the user doesn't want to see the rationale any more
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    PermissionRequired(
        permissionState = locationPermissionState,
        permissionNotGrantedContent = {
            // user has location permission disabled
            if (doNotShowRationale) {
                Toast.makeText(context, "Go to app's settings and enable the location", Toast.LENGTH_LONG).show()
                DisplayMap(
                    context = context,
                    mapView = mapView,
                    latitude = latitude,
                    longitude = longitude,
                    title = title,
                    locationEnabled = false,
                    permissionManager = PackageManager.PERMISSION_DENIED,
                    modifier = modifier
                )
            }
            // asking for permission
            else {
                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = { openDialog.value = false },
                        title = { Text(text = "Current location permission request", fontWeight = FontWeight.Bold) },
                        text = { Text("The current location is important for this app. Please grant the permission.") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                    locationPermissionState.launchPermissionRequest()
                                }) {
                                Text(text = "Confirm", color = MaterialTheme.colors.primaryVariant)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                    doNotShowRationale = true
                                }
                            ) {
                                Text(text = "Dismiss", color = MaterialTheme.colors.primaryVariant)
                            }
                        },
                    )
                }
            }
        },
        permissionNotAvailableContent = {
            Column {
                Toast.makeText(context, "Go to app's settings and enable the location", Toast.LENGTH_LONG).show()
                DisplayMap(
                    context = context,
                    mapView = mapView,
                    latitude = latitude,
                    longitude = longitude,
                    title = title,
                    locationEnabled = false,
                    permissionManager = PackageManager.PERMISSION_DENIED,
                    modifier = modifier
                )
            }
        },
        content = {
            // user has location permission enabled
            DisplayMap(
                context = context,
                mapView = mapView,
                latitude = latitude,
                longitude = longitude,
                title = title,
                locationEnabled = true,
                permissionManager = PackageManager.PERMISSION_GRANTED,
                modifier = modifier
            )
        }
    )
}

// Google Maps
@Composable
fun DisplayMap(
    context: Context, mapView: MapView, latitude: Double,
    longitude: Double, title: String, locationEnabled: Boolean,
    permissionManager: Int, modifier: Modifier ) {
    AndroidView(modifier = modifier, factory = { mapView }) { map ->
        map.getMapAsync {
            val coordinates = LatLng(latitude, longitude)
            it.addMarker(MarkerOptions().position(coordinates).title(title))
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 18f), 4000, null)

            // condition to know when the location permission has been granted
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == permissionManager) {
                // enable real-time location in map
                it.isMyLocationEnabled = locationEnabled
            }
        }
    }
}

@ExperimentalPermissionsApi
@Preview(showBackground = true)
@Composable
fun AudioGuideMapPreview() {
    GuideMeTravelersAppTheme {
        AudioGuideMapContent()
    }
}
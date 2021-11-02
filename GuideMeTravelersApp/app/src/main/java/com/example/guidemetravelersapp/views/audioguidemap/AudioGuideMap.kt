package com.example.guidemetravelersapp.views.audioguidemap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.Location
import com.example.guidemetravelersapp.helpers.commonComposables.AutoCompleteTextView
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.viewModels.LocationViewModel
import com.example.guidemetravelersapp.views.experienceDetailsView.DescriptionTags
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch

class AudioGuideMap : ComponentActivity() {
    @ExperimentalMaterialApi
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

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun AudioGuideMapContent(navController: NavHostController? = null, locationViewModel: LocationViewModel = viewModel()) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(color = colors.primary),
                contentAlignment = Alignment.Center,
                content = {
                    Text(
                        text = "Available places with audio guides",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            )
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if(locationViewModel.locations.data != null) {
                    itemsIndexed(locationViewModel.locations.data!!) { index, item ->
                        LocationCard(
                            location = item, tags = listOf("cultural"), navController!!
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        },
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) scaffoldState.bottomSheetState.expand()
                        else scaffoldState.bottomSheetState.collapse() } },
                content = {
                    if (scaffoldState.bottomSheetState.isCollapsed)
                        Icon(Icons.Default.Expand, contentDescription = "Open bottom sheet scaffold", tint = Color.White)
                    else Icon(Icons.Default.Close, contentDescription = "Close bottom sheet scaffold", tint = Color.White)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetPeekHeight = 100.dp,
        backgroundColor = Color.Unspecified
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            content = {
                MapSearchView(textState, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 10.dp),
                    navController,
                locationViewModel)
                MapScreen(
                    modifier = Modifier.fillMaxHeight(),
                    latitude = "50.94135408574933",
                    longitude = "6.958866858783249",
                    title = "Cologne Cathedral"
                )
            }
        )
    }
}

@Composable
fun MapSearchView(
    textState: MutableState<TextFieldValue>,
    modifier: Modifier, navController:
    NavHostController? = null,
    model: LocationViewModel
) {
    val focusManager = LocalFocusManager.current
    var addressList: List<Address>?
    val context = LocalContext.current

    AutoCompleteTextView(
        modifier = modifier,
        query = textState.value.text,
        queryLabel = stringResource(id = R.string.search_by_location),
        predictions = model.predictions,
        onQueryChanged = { updateAddress ->
            textState.value = TextFieldValue(updateAddress)
            model.onQueryChanged(updateAddress)
        },
        onClearClick = {
            textState.value = TextFieldValue("")
            model.locationSearchValue = ""
            model.predictions = mutableListOf()
        },
        onItemClick = { place ->
            textState.value = TextFieldValue(place.getPrimaryText(null).toString())
            model.onPlaceItemSelected(place)
        }
    ) {
        Text("${it.getFullText(null)}", style = MaterialTheme.typography.caption)
    }

//    TextField(
//        value = textState.value,
//        onValueChange = { value -> textState.value = value },
//        modifier = modifier,
//        textStyle = MaterialTheme.typography.caption,
//        label = { Text(text = stringResource(id = R.string.search_by_location), style = MaterialTheme.typography.caption) },
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Text,
//            imeAction = ImeAction.Search
//        ),
//        keyboardActions = KeyboardActions(
//            onSearch = {
//                val geoCoder = Geocoder(context)
//                addressList = geoCoder.getFromLocationName(textState.value.text, 1)
//                val address = addressList!![0]
//                navController?.navigate("searchMap/${address.latitude}/${address.longitude}/${textState.value.text}")
//                focusManager.clearFocus()
//            }
//        ),
//        leadingIcon = {
//            Icon(
//                Icons.Default.Search,
//                contentDescription = "Search",
//            )
//        },
//        singleLine = true,
//        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Unspecified)
//    )
}

@Composable
//TODO: remove tag list as parameter
fun LocationCard(location: Location, tags: List<String>, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = colors.secondary),
                onClick = { navController.navigate("locationDetails/${location.id}") }
            ),
        elevation = 10.dp,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
                if(location.locationPhotoUrl.isEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.dummy_avatar),
                        contentDescription = "Temporal dummy avatar",
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .height(80.dp)
                    )
                } else {
                    Box(modifier = Modifier
                        .size(130.dp)) {
                        CoilImage(
                            imageModel = location.locationPhotoUrl,
                            contentDescription = "Location photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .height(120.dp)
                        )
                    }
                }
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    Text(
                        text = location.name,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)) {
                        for (tag in tags) {
                            DescriptionTags(tagName = tag)
                        }
                    }
                }
            }
        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun MapScreen(modifier: Modifier? = Modifier, latitude: String = "", longitude: String = "", title: String = "") {
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
                    latitude = latitude.toDouble(),
                    longitude = longitude.toDouble(),
                    title = title,
                    locationEnabled = false,
                    permissionManager = PackageManager.PERMISSION_DENIED,
                    modifier = modifier!!
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
                                Text(text = "Confirm", color = colors.primaryVariant)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                    doNotShowRationale = true
                                }
                            ) {
                                Text(text = "Dismiss", color = colors.primaryVariant)
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
                    latitude = latitude.toDouble(),
                    longitude = longitude.toDouble(),
                    title = title,
                    locationEnabled = false,
                    permissionManager = PackageManager.PERMISSION_DENIED,
                    modifier = modifier!!
                )
            }
        },
        content = {
            // user has location permission enabled
            DisplayMap(
                context = context,
                mapView = mapView,
                latitude = latitude.toDouble(),
                longitude = longitude.toDouble(),
                title = title,
                locationEnabled = true,
                permissionManager = PackageManager.PERMISSION_GRANTED,
                modifier = modifier!!
            )
        }
    )
}

// Google Maps
@Composable
fun DisplayMap(
    context: Context, mapView: MapView, latitude: Double,
    longitude: Double, title: String, locationEnabled: Boolean,
    permissionManager: Int, modifier: Modifier) {
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

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Preview(showBackground = true)
@Composable
fun AudioGuideMapPreview() {
    GuideMeTravelersAppTheme {
        AudioGuideMapContent()
    }
}
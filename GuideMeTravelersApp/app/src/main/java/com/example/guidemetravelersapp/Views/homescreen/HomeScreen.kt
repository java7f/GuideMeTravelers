package com.example.guidemetravelersapp.views.homescreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.ui.theme.CancelRed
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeScreen : ComponentActivity() {
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeTravelersAppTheme {
                HomeScreenContent()
            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun HomeScreenContent() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope() // handles open() & close() suspend functions

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(scaffoldState, scope) },
        content = { ScaffoldContent() },
        drawerContent = { NavDrawer(scaffoldState, scope) },
        drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
    )
}

@Composable
fun AppBar(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    TopAppBar(
        title = { Icon(painter = painterResource(id = R.drawable.logo_transparent), contentDescription = "Guide Me logo", modifier = Modifier.fillMaxWidth() ) },
        navigationIcon = {
            IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Drawer menu")
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Language, contentDescription = "Translate")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    )
}

@ExperimentalPermissionsApi
@Composable
fun ScaffoldContent() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column(modifier = Modifier.fillMaxSize()) {
        SearchView(textState)
        MapScreen(
            50.937616532313434,
            6.960581381481977,
            "Cologne Cathedral",
            modifier = Modifier.height(300.dp))
        /* TODO:
        *   - Finish homescreen display with mock data
        *   - Make map clicklable (full size map when clicking on it)
        *   - Find out if drawer can be limited to just opening when clicking on the button
        *     - if not, make menu on another view
        *   - Make bottom navigation menu  */
    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = state.value,
        onValueChange = { value -> state.value = value },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        textStyle = TextStyle(fontSize = 18.sp),
        label = { Text(text = "Search") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                // execute search function
                focusManager.clearFocus()
            }
        ),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
            )
        },
        singleLine = true,
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

@Composable
fun NavDrawer(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        Row(modifier = Modifier.weight(4f)) {
            Column {
                UserCard()
                Divider(thickness = 2.dp)
                NavOption(title = "Map", scaffoldState = scaffoldState, scope)
                NavOption(title = "History", scaffoldState = scaffoldState, scope)
                NavOption(title = "Your Guides", scaffoldState = scaffoldState, scope)
                NavOption(title = "Become a Guide", scaffoldState = scaffoldState, scope)
                NavOption(title = "Alerts", scaffoldState = scaffoldState, scope)
                NavOption(title = "Account Settings", scaffoldState = scaffoldState, scope)
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            Column {
                Divider(thickness = 2.dp)
                NavOption(title = "Send Feedback", scaffoldState = scaffoldState, scope)
                Text(
                    text = "Logout",
                    style = TextStyle(color = CancelRed, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = MaterialTheme.colors.secondary),
                            onClick = { scope.launch { scaffoldState.drawerState.close() } }
                        )
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun UserCard() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 20.dp)) {
        Image(
            painter = painterResource(R.drawable.dummy_avatar),
            contentDescription = "Temporal dummy avatar",
            modifier = Modifier
                .clip(CircleShape)
                .height(60.dp)
        )
        Column(modifier = Modifier.padding(start = 20.dp)) {
            Text(
                text = "Nombre Apellido",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Text(text = "Usuario")
        }
    }
}

@Composable
fun NavOption(title: String, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    Text(
        text = title,
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = { scope.launch { scaffoldState.drawerState.close() } }
            )
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@ExperimentalPermissionsApi
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GuideMeTravelersAppTheme {
        HomeScreenContent()
    }
}
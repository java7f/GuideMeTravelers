package com.example.guidemetravelersapp.homescreen

import android.os.Bundle
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
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.ui.theme.CancelRed
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeTravelersAppTheme {
                HomeScreenContent()
            }
        }
    }
}

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

@Composable
fun ScaffoldContent() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    //SearchView(textState)
    MapScreen(50.937616532313434, 6.960581381481977, "Cologne Cathedral") // google maps
    // TODO: add location permissions
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = state.value,
        onValueChange = { value -> state.value = value },
        modifier = Modifier.fillMaxWidth().padding(20.dp),
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

// Google Maps
@Composable
fun MapScreen(latitude: Double, longitude: Double, title: String) {
    val mapView = rememberMapViewWithLifecycle()
    val context = LocalContext.current

    AndroidView({ mapView }) { map ->
        map.getMapAsync {
            val coordinates = LatLng(latitude, longitude)
            it.addMarker(MarkerOptions().position(coordinates).title(title))
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 18f), 4000, null)

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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GuideMeTravelersAppTheme {
        HomeScreenContent()
    }
}
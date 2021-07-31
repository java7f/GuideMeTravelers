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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.ui.theme.CancelRed
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.views.audioguidemap.AudioGuideMap
import com.example.guidemetravelersapp.views.audioguidemap.AudioGuideMapContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
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
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(scaffoldState, scope) },
        content = { ScreenController(navController) },
        drawerContent = { NavDrawer(scaffoldState, scope) },
        drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
        drawerGesturesEnabled = false,
        bottomBar = { BottomBar(navController) }
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
        Text(
            text = "Available guides",
            modifier = Modifier.padding(horizontal = 15.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        /* TODO:
        *   - Finish homescreen display with mock data
        *     - Make another UserCard with rating and tags.
        *   - Make map clickable (full size map when clicking on it)
        *   - Add navigation to drawer elements (the ones that can be added) */
    }
}

@Composable
fun SearchView(textState: MutableState<TextFieldValue>) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = textState.value,
        onValueChange = { value -> textState.value = value },
        modifier = Modifier.fillMaxWidth().padding(15.dp).height(60.dp),
        textStyle = TextStyle(fontSize = 16.sp),
        label = { Text(text = "Search") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                // TODO: execute search function
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

@Composable
fun NavDrawer(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        Row(modifier = Modifier.weight(4f)) {
            Column {
                UserCard("Pepito", "Perez", "pepitop24")
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
fun UserCard(name: String, lastname: String, username: String) {
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
                text = "$name $lastname",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Text(text = username)
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

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(BottomNavScreen.Map, BottomNavScreen.AudioGuide, BottomNavScreen.Chat)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        content = { items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(
                    painter = painterResource(id = screen.resId),
                    contentDescription = screen.label,
                    modifier = Modifier.height(30.dp)
                ) },
                selected = currentRoute == screen.route,
                unselectedContentColor = MaterialTheme.colors.secondary,
                selectedContentColor = MaterialTheme.colors.secondaryVariant,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = false
                    }

                })
        } }
    )
}

@ExperimentalPermissionsApi
@Composable
fun ScreenController(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "guides",
        builder = {
            composable(route = "guides", content = { ScaffoldContent() })
            composable(route = "map", content = { AudioGuideRouteTest() })
            composable(route = "chat", content = { ChatRouteTest() })
        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun AudioGuideRouteTest() {
    AudioGuideMapContent()
}

@Composable
fun ChatRouteTest() {
    Text("Chat Guide Route Text")
}

@ExperimentalPermissionsApi
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GuideMeTravelersAppTheme {
        HomeScreenContent()
    }
}
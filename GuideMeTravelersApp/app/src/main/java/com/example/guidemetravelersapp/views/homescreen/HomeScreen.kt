package com.example.guidemetravelersapp.views.homescreen

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.viewData.GuideExperienceViewData
import com.example.guidemetravelersapp.helpers.ASBLeScannerWrapper
import com.example.guidemetravelersapp.helpers.RoutineManager
import com.example.guidemetravelersapp.helpers.ScannerCallback
import com.example.guidemetravelersapp.helpers.SessionManager
import com.example.guidemetravelersapp.helpers.commonComposables.AutoCompleteTextView
import com.example.guidemetravelersapp.helpers.commonComposables.FullsizeImage
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingBar
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.example.guidemetravelersapp.helpers.utils.Utils
import com.example.guidemetravelersapp.services.LocationService
import com.example.guidemetravelersapp.ui.theme.*
import com.example.guidemetravelersapp.viewModels.HomescreenViewModel
import com.example.guidemetravelersapp.viewModels.ProfileViewModel
import com.example.guidemetravelersapp.views.audioGuideLocation.LocationContent
import com.example.guidemetravelersapp.views.experienceDetailsView.DescriptionTags
import com.example.guidemetravelersapp.views.experienceDetailsView.GuideDescriptionExperience
import com.example.guidemetravelersapp.views.experienceDetailsView.GuideRating
import com.example.guidemetravelersapp.views.audioguidemap.AudioGuideMapContent
import com.example.guidemetravelersapp.views.audioguidemap.DisplayMap
import com.example.guidemetravelersapp.views.experienceHistoryView.ShowPastExperiences
import com.example.guidemetravelersapp.views.audioguidemap.MapScreen
import com.example.guidemetravelersapp.views.chatView.ChatList
import com.example.guidemetravelersapp.views.chatView.ChatView
import com.example.guidemetravelersapp.views.experienceDetailsView.reservationRequest.ReservationRequestContent
import com.example.guidemetravelersapp.views.experienceHistoryView.ReservationsManagement
import com.example.guidemetravelersapp.views.profileView.EditProfileContent
import com.example.guidemetravelersapp.views.profileView.UserProfileInformation
import com.example.guidemetravelersapp.views.touristAlertsViews.AlertsManagement
import com.example.guidemetravelersapp.views.touristAlertsViews.CreateTouristAlert
import com.example.guidemetravelersapp.views.wishlist.WishlistContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.libraries.places.api.Places
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeScreen : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(application, "AIzaSyAn7Hyeg5O-JKSoKUXRmG_I-KMThIDBcDI")
        ASBLeScannerWrapper.initializeInstance(this, ScannerCallback)
        setContent {
            GuideMeTravelersAppTheme {
                HomeScreenContent()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun HomeScreenContent() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope() // handles open() & close() suspend functions
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(scaffoldState, scope) },
        content = { innerPadding -> Box(modifier = Modifier.padding(innerPadding)) {
            ScreenController(navController) }
        },
        drawerContent = { NavDrawer(scaffoldState, scope, navController) },
        drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
        drawerGesturesEnabled = false,
        bottomBar = { BottomBar(navController) },
        backgroundColor = Color.Unspecified
    )
}

@Composable
fun AppBar(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    TopAppBar(
        title = {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ) {
                Icon(painter = painterResource(id = R.drawable.logo_transparent),
                    contentDescription = "Guide Me logo",
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Drawer menu")
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Language, contentDescription = "Translate", tint = Color.Transparent)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.Transparent)
            }
        },
        contentColor = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.surface
    )
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@Composable
fun ScaffoldContent(navController: NavHostController, model: HomescreenViewModel = viewModel()) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(true) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)    // Track if the user doesn't want to see the rationale any more
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    PermissionRequired(
        permissionState = locationPermissionState,
        permissionNotGrantedContent = {
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDialog.value = false },
                    title = { Text(text = stringResource(id = R.string.location_permission_title), fontWeight = FontWeight.Bold) },
                    text = { Text(stringResource(id = R.string.location_permission_content)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                locationPermissionState.launchPermissionRequest()
                            }) {
                            Text(text = stringResource(id = R.string.confirm_request), color = MaterialTheme.colors.primaryVariant)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                doNotShowRationale = true
                            }
                        ) {
                            Text(text = stringResource(id = R.string.dismiss_permission), color = MaterialTheme.colors.primaryVariant)
                        }
                    },
                )
            }
        },
        permissionNotAvailableContent = {
            Column {
                Toast.makeText(context, stringResource(id = R.string.ondismiss_message), Toast.LENGTH_LONG).show()
                Text(text = stringResource(id = R.string.ondismiss_message),
                    modifier = Modifier.padding(bottom = 15.dp),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Bold)
            }
        },
        content = {
            // user has location permission enabled
            model.fetchExperiencesViewData()
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
                state = listState) {
                item {
                    SearchView(textState,
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        model
                    )
                    Text(text = stringResource(id = R.string.available_guides) + " ${model.currentCityLocation}",
                        modifier = Modifier.padding(bottom = 15.dp),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Bold)
                    if (model.guideExperienceViewData.data.isNullOrEmpty() && !model.guideExperienceViewData.inProgress) {
                        Text(text = stringResource(id = R.string.no_guides))
                    }
                }
                if (model.guideExperienceViewData.inProgress) {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LoadingSpinner()
                        }
                    }
                } else {
                    if (!model.guideExperienceViewData.data.isNullOrEmpty()) {
                        itemsIndexed(model.guideExperienceViewData.data!!) { index, item ->
                            UserCard(item, imgSize = 70.dp, navController = navController)
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SearchView(textState: MutableState<TextFieldValue>, modifier: Modifier, model: HomescreenViewModel) {

    AutoCompleteTextView(
        modifier = modifier,
        query = textState.value.text,
        queryLabel = stringResource(id = R.string.search_by_location),
        onQueryChanged = { updateAddress ->
            textState.value = TextFieldValue(updateAddress)
            model.onQueryChanged(updateAddress)
         },
        predictions = model.predictions,
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
}

@Composable
fun NavDrawer(scaffoldState: ScaffoldState,
              scope: CoroutineScope,
              navController: NavHostController,
              profileViewModel: ProfileViewModel = viewModel()) {
    val isOffLineMode = remember { mutableStateOf(profileViewModel.getOfflineMode())}
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        Row(modifier = Modifier.weight(4f)) {
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    content = {
                        if(!profileViewModel.profileData.inProgress && !profileViewModel.profileData.hasError) {
                            UserCard(
                                name = profileViewModel.profileData.data!!.firstName,
                                lastname = profileViewModel.profileData.data!!.lastName,
                                username = profileViewModel.profileData.data!!.username,
                                imgSize = 60.dp,
                                navController = navController,
                                navRoute = "profile",
                                imageUrl = profileViewModel.profileData.data!!.profilePhotoUrl,
                                scaffoldState = scaffoldState,
                                scope = scope
                            )
                        }
                        Icon(
                            Icons.Default.MenuOpen,
                            contentDescription = "Menu Open",
                            modifier = Modifier.clickable(onClick = { scope.launch { scaffoldState.drawerState.close() } })
                        )
                    }
                )
                Divider(thickness = 2.dp)
                NavOption(title = stringResource(id = R.string.guides), scaffoldState = scaffoldState, scope, navController, "guides")
                NavOption(title = stringResource(id = R.string.reservations_overview), scaffoldState = scaffoldState, scope, navController, "past_experiences")
                NavOption(title = stringResource(id = R.string.wishlist), scaffoldState = scaffoldState, scope, navController, "wishlist")
                NavOption(title = stringResource(id = R.string.tourist_alerts), scaffoldState = scaffoldState, scope, navController, "tourist_alerts")
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)) {
                    Text(
                        text = stringResource(id = R.string.offline_mode),
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                    Switch(
                        checked = isOffLineMode.value,
                        onCheckedChange = {
                            isOffLineMode.value = it
                            profileViewModel.saveOfflineMode(it)
                        }
                    )
                }
                Divider(thickness = 2.dp)
                Text(
                    text = stringResource(id = R.string.logout),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onError,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = MaterialTheme.colors.secondary),
                            onClick = {
                                profileViewModel.signOutUser()
                            }
                        )
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

/* User card with standard information */
@Composable
fun UserCard(
    name: String,
    lastname: String,
    username: String,
    imgSize: Dp,
    navController: NavHostController,
    navRoute: String = "",
    imageUrl: String = "",
    scaffoldState: ScaffoldState,
    scope: CoroutineScope
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 20.dp)
            .clickable {
                scope.launch { scaffoldState.drawerState.close() }
                navController.navigate(navRoute)
            })
    {
        if(imageUrl.isEmpty()) {
            Image(
                painter = painterResource(R.drawable.dummy_avatar),
                contentDescription = "Temporal dummy avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .height(imgSize)
            )
        } else {
            Box(modifier = Modifier
                .size(imgSize)
                .border(2.dp, Teal200, CircleShape)) {
                    CoilImage(imageModel = imageUrl,
                        contentDescription = "User profile photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clip(CircleShape))
            }
        }
        Column(modifier = Modifier.padding(start = 20.dp)) {
            Text(
                text = "$name $lastname",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Text(text = username)
        }
    }
}

/* User card with standard user information, excluding username and adding rating and tags */
@Composable
fun UserCard(experienceViewData: GuideExperienceViewData, imgSize: Dp, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = { navController.navigate("guideExperience/${experienceViewData.id}") }
            ),
        elevation = 5.dp,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
                if(experienceViewData.guidePhotoUrl.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.dummy_avatar),
                        contentDescription = "Temporal dummy avatar",
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(imgSize)
                    )
                }
                else {
                    Box(modifier = Modifier
                        .size(100.dp)
                        .border(2.dp, Teal200, CircleShape)) {
                        CoilImage(
                            imageModel = experienceViewData.guidePhotoUrl,
                            contentDescription = "Guide photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.clip(CircleShape)
                        )
                    }
                }
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    Text(
                        text = "${experienceViewData.guideFirstName} ${experienceViewData.guideLastName}",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .padding(end = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${experienceViewData.guideAddress.city}, ${experienceViewData.guideAddress.country}",
                            style = MaterialTheme.typography.overline,
                        )
                    }
                    GuideRating(experienceViewData.guideRating)
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)) {
                        for (tag in experienceViewData.experienceTags) {
                            DescriptionTags(tagName = tag)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun NavOption(title: String, scaffoldState: ScaffoldState, scope: CoroutineScope, navController: NavHostController, navRoute: String = "") {
    Text(
        text = title,
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = {
                    scope.launch { scaffoldState.drawerState.close() }
                    navController.navigate(navRoute)
                }
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
        content = { items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(
                    painter = painterResource(id = screen.resId),
                    contentDescription = screen.label,
                    modifier = Modifier.height(25.dp)
                ) },
                selected = currentRoute == screen.route,
                selectedContentColor = Color.White,
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

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun ScreenController(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "guides",
        builder = {
            composable(route = "guides", content = { ScaffoldContent(navController) })
            composable(route = "tourist_alerts", content = { AlertsManagement(navController) })
            composable(route = "add_tourist_alert", content = { CreateTouristAlert(navController) })
            composable(route = "map", content = { AudioGuideMapContent(navController = navController) })
            composable(route = "chat", content = { ChatList(navController = navController) })
            composable(route = "wishlist", content = { WishlistContent(navHostController = navController) })
            composable(route = "guideExperience/{experienceId}", content = { backStackEntry ->
                GuideDescriptionExperience(backStackEntry.arguments?.getString("experienceId")!!, navController)
            })
            composable(route = "locationDetails/{locationId}", content = { backStackEntry ->
                LocationContent(backStackEntry.arguments?.getString("locationId")!!)
            })
            composable(route = "editProfile", content = { EditProfileContent() })

            composable(route = "profile", content = { UserProfileInformation(navController = navController) })

            composable(route = "past_experiences", content = { ReservationsManagement() })

            composable(route = "profile_photo/photo={photo_url}", content = { backStackEntry ->
                FullsizeImage(backStackEntry.arguments?.getString("photo_url")!!)
            })
            composable(route = "chat_with/{sentTo_Id}", content = { backStackEntry ->
                ChatView(backStackEntry.arguments?.getString("sentTo_Id")!!)
            })
            composable(route = "request_reservation/{experienceId}", content = { backStackEntry ->
                ReservationRequestContent(backStackEntry.arguments?.getString("experienceId")!!, navController)
            })
        }
    )
}

@Composable
fun ChatRouteTest() {
    Text("Chat Guide Route Text")
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GuideMeTravelersAppTheme {
        HomeScreenContent()
    }
}
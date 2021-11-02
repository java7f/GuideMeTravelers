package com.example.guidemetravelersapp.views.homescreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.viewData.GuideExperienceViewData
import com.example.guidemetravelersapp.helpers.commonComposables.AutoCompleteTextView
import com.example.guidemetravelersapp.helpers.commonComposables.FullsizeImage
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingBar
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.ui.theme.MilitaryGreen200
import com.example.guidemetravelersapp.viewModels.HomescreenViewModel
import com.example.guidemetravelersapp.viewModels.ProfileViewModel
import com.example.guidemetravelersapp.views.audioGuideLocation.LocationContent
import com.example.guidemetravelersapp.views.experienceDetailsView.DescriptionTags
import com.example.guidemetravelersapp.views.experienceDetailsView.GuideDescriptionExperience
import com.example.guidemetravelersapp.views.experienceDetailsView.GuideRating
import com.example.guidemetravelersapp.views.audioguidemap.AudioGuideMapContent
import com.example.guidemetravelersapp.views.experienceHistoryView.ShowPastExperiences
import com.example.guidemetravelersapp.views.audioguidemap.MapScreen
import com.example.guidemetravelersapp.views.chatView.ChatList
import com.example.guidemetravelersapp.views.chatView.ChatView
import com.example.guidemetravelersapp.views.experienceDetailsView.reservationRequest.ReservationRequestContent
import com.example.guidemetravelersapp.views.profileView.EditProfileContent
import com.example.guidemetravelersapp.views.profileView.UserProfileInformation
import com.example.guidemetravelersapp.views.wishlist.WishlistContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeScreen : ComponentActivity() {
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: HomescreenViewModel by viewModels()
        setContent {
            GuideMeTravelersAppTheme {
                HomeScreenContent(model)
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun HomeScreenContent(model: HomescreenViewModel? = null) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope() // handles open() & close() suspend functions
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(scaffoldState, scope) },
        content = { innerPadding -> Box(modifier = Modifier.padding(innerPadding)) {
            ScreenController(navController, model!!) }
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
        contentColor = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.surface
    )
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@Composable
fun ScaffoldContent(navController: NavHostController, model: HomescreenViewModel) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()
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
                Text(text = "Available guides",
                    modifier = Modifier.padding(bottom = 15.dp),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Bold)
            }
            if (model.guideExperienceViewData.inProgress) {
                item {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LoadingSpinner()
                    }
                }
            } else {
                if (model.guideExperienceViewData.data != null) {
                    itemsIndexed(model.guideExperienceViewData.data!!) { index, item ->
                        UserCard(item, imgSize = 70.dp, navController = navController)
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }
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
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        Row(modifier = Modifier.weight(4f)) {
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    content = {
                        if(!profileViewModel.profileData.inProgress) {
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
                NavOption(title = "Guides", scaffoldState = scaffoldState, scope, navController, "guides")
                NavOption(title = "History", scaffoldState = scaffoldState, scope, navController, "past_experiences")
                NavOption(title = "Wishlist", scaffoldState = scaffoldState, scope, navController, "wishlist")
                NavOption(title = "Become a Guide", scaffoldState = scaffoldState, scope, navController)
                NavOption(title = "Alerts", scaffoldState = scaffoldState, scope, navController)
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            Column {
                Divider(thickness = 2.dp)
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onError,
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
                .border(2.dp, MilitaryGreen200, CircleShape)) {
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
                            .clip(CircleShape)
                            .height(imgSize)
                    )
                }
                else {
                    Box(modifier = Modifier
                        .size(70.dp)) {
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
                        fontWeight = FontWeight.Bold
                    )
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

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun ScreenController(navController: NavHostController, model: HomescreenViewModel) {
    NavHost(
        navController = navController,
        startDestination = "guides",
        builder = {
            composable(route = "guides", content = { ScaffoldContent(navController, model) })
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

            composable(route = "past_experiences", content = { ShowPastExperiences() })

            composable(route = "profile_photo/photo={photo_url}", content = { backStackEntry ->
                FullsizeImage(backStackEntry.arguments?.getString("photo_url")!!)
            })
            composable(route = "chat_with/{sentTo_Id}", content = { backStackEntry ->
                ChatView(backStackEntry.arguments?.getString("sentTo_Id")!!)
            })
            composable(route = "request_reservation/{experienceId}", content = { backStackEntry ->
                ReservationRequestContent(backStackEntry.arguments?.getString("experienceId")!!, navController)
            })
            composable(
                route = "searchMap/{latitude}/{longitude}/{title}",
                content = { backStackEntry -> MapScreen(
                    latitude = backStackEntry.arguments?.getString("latitude")!!,
                    longitude = backStackEntry.arguments?.getString("longitude")!!,
                    title = backStackEntry.arguments?.getString("title")!!
                )}
            )
        }
    )
}

@Composable
fun ChatRouteTest() {
    Text("Chat Guide Route Text")
}

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
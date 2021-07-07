package com.example.guidemetravelersapp.homescreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guidemetravelersapp.homescreen.ui.theme.GuideMeTravelersAppTheme
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
        topBar = { AppBar(title = "Sample", scaffoldState, scope) },
        content = { },
        drawerContent = { NavDrawer(scaffoldState, scope) },
        drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
    )
}

@Composable
fun AppBar(title: String, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Drawer menu")
            }
        }
    )
}

@Composable
fun NavOption(title: String, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    Text(
        text = title,
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = Color.Yellow),
            onClick = { scope.launch { scaffoldState.drawerState.close() } }
        ).padding(16.dp).fillMaxWidth()
    )
}

@Composable
fun NavDrawer(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    Column {
        NavOption(title = "Experience history", scaffoldState = scaffoldState, scope)
        NavOption(title = "Your Guides", scaffoldState = scaffoldState, scope)
        NavOption(title = "Become a guide", scaffoldState = scaffoldState, scope)
        NavOption(title = "Alerts", scaffoldState = scaffoldState, scope)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GuideMeTravelersAppTheme {
        HomeScreenContent()
    }
}
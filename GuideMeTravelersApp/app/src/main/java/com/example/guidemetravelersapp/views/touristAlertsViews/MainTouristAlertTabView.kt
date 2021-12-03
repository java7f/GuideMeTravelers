package com.example.guidemetravelersapp.views.touristAlertsViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.R

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertsManagement(navHostController: NavHostController) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabData = listOf(
        stringResource(id = R.string.open_alerts),
        stringResource(id = R.string.guide_purposal),
    )
    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabData.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    },
                    modifier = Modifier.padding(vertical = 5.dp),
                )
            }
        }
        when(tabIndex) {
            0 -> TouristAlertsList(navHostController)
            1 -> GuidingOffers(navHostController)
        }
    }
}
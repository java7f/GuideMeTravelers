package com.example.guidemetravelersapp.views.touristAlertsViews

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Explore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.guidemetravelersapp.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.dataModels.TouristAlert
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.example.guidemetravelersapp.ui.theme.CancelRed
import com.example.guidemetravelersapp.viewModels.ReservationViewModel
import com.example.guidemetravelersapp.views.experienceDetailsView.DescriptionTags
import java.text.SimpleDateFormat

@ExperimentalFoundationApi
@Composable
fun TouristAlertsList(navHostController: NavHostController, reservationViewModel: ReservationViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .fillMaxSize(),
        content = {
            reservationViewModel.getTouristAlerts()
            stickyHeader {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    OutlinedButton(
                        onClick = {
                            navHostController.navigate("add_tourist_alert")
                        },
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add alert",
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = stringResource(id = R.string.add_alert),
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
            if (reservationViewModel.touristAlerts.inProgress) {
                item {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LoadingSpinner()
                    }
                }
            }
            else {
                if(!reservationViewModel.touristAlerts.data.isNullOrEmpty()) {
                    itemsIndexed(reservationViewModel.touristAlerts.data!!) { _, item ->
                        TouristAlert(
                            touristAlert = item,
                            touristAlertViewModel = reservationViewModel
                        )
                        Spacer(modifier = Modifier.padding(bottom = 10.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun TouristAlert(touristAlert: TouristAlert, touristAlertViewModel: ReservationViewModel) {
    val pattern = "MMM dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    val from: String = simpleDateFormat.format(touristAlert.fromDate)
    val to: String = simpleDateFormat.format(touristAlert.toDate)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 10.dp,
        content = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Text(
                                text = stringResource(id = R.string.alert_in) + ": ${touristAlert.touristDestination}",
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.onSecondary,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedButton(
                                onClick = {  },
                                border = BorderStroke(1.dp, color = CancelRed),
                                content = {
                                    Icon(imageVector = Icons.Default.DoNotDisturb,
                                        contentDescription = "Guide",
                                        tint = CancelRed)
                                    Text(
                                        text = stringResource(id = R.string.cancel),
                                        color = CancelRed,
                                        modifier = Modifier.padding(start = 5.dp),
                                        style = MaterialTheme.typography.caption
                                    )
                                }
                            )
                        }
                    )
                    Text(text = stringResource(id = R.string.destination) + ": ${touristAlert.touristDestination}",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSecondary,)
                    Row(
                        modifier = Modifier.padding(top = 10.dp,bottom = 10.dp),
                        content = {
                            Icon(imageVector = Icons.Default.EventAvailable, contentDescription = "Dates")
                            Text(modifier = Modifier.padding(start = 5.dp), text = "$from - $to",
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.onSecondary)
                        }
                    )
                    Row(
                        modifier = Modifier.padding(bottom = 10.dp),
                        content = {
                            Text(text = stringResource(id = R.string.languages) + ":", color = MaterialTheme.colors.onSecondary)
                            Row(
                                modifier = Modifier.fillMaxWidth()) {
                                for (language in touristAlert.touristLanguages) {
                                    Text(modifier = Modifier.padding(start = 5.dp), text = language,
                                        style = MaterialTheme.typography.subtitle2,
                                        color = MaterialTheme.colors.onSecondary)
                                }
                            }
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth()) {
                        for (tag in touristAlert.experienceTags) {
                            DescriptionTags(tagName = tag)
                        }
                    }
                }
            )
        }
    )
}
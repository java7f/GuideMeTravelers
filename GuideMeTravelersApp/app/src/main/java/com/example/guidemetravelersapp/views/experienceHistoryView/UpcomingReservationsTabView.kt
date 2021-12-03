package com.example.guidemetravelersapp.views.experienceHistoryView

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.ExperienceReservation
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.example.guidemetravelersapp.viewModels.ReservationViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@ExperimentalFoundationApi
@Composable
fun ShowUpcomingReservations(reservationViewModel: ReservationViewModel = viewModel()) {
    val isRefreshingUpcomingExperiences by reservationViewModel.isRefreshingUpcomingExperiences.collectAsState()

    reservationViewModel.getUpcomingExperiences()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshingUpcomingExperiences),
        onRefresh = { reservationViewModel.refreshUpcomingExperiences() },
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize())  {
                item {
                    if(reservationViewModel.upcomingExperienceReservations.data.isNullOrEmpty() && !reservationViewModel.upcomingExperienceReservations.inProgress) {
                        Text(modifier = Modifier.padding(15.dp), text = stringResource(id = R.string.no_upcoming_trips))
                    }
                }
                if (reservationViewModel.upcomingExperienceReservations.inProgress) {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LoadingSpinner()
                        }
                    }
                } else {
                    if(!reservationViewModel.upcomingExperienceReservations.data.isNullOrEmpty()) {
                        itemsIndexed(reservationViewModel.upcomingExperienceReservations.data!!) { index, item ->
                            Card(
                                modifier = Modifier.padding(15.dp),
                                elevation = 15.dp,
                                content = {
                                    UpcomingReservationsCardContent(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UpcomingReservationsCardContent(experiencieReservation: ExperienceReservation) {
    val from = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(experiencieReservation.fromDate.time).atZone(
                ZoneId.systemDefault()).toLocalDate().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            ))
    ) }
    val to = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(experiencieReservation.toDate.time).atZone(
                ZoneId.systemDefault()).toLocalDate().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            ))
    ) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.guide_name) +": ",
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${experiencieReservation.guideFirstName} ${experiencieReservation.guideLastName}",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.destination) +": ",
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = experiencieReservation.address.city,
                color = MaterialTheme.colors.onSecondary,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.from_to) +": ${from.value.text} / ${to.value.text}",
                color = MaterialTheme.colors.onSecondary
            )

        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.price) +": ",
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = experiencieReservation.price.toString(),
                color = MaterialTheme.colors.onSecondary,
                style = TextStyle()
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}


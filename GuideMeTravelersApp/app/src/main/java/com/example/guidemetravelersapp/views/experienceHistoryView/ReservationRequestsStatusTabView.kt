package com.example.guidemetravelersapp.views.experienceHistoryView

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.ExperienceReservationRequest
import com.example.guidemetravelersapp.dataModels.ReservationStatus
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.example.guidemetravelersapp.ui.theme.AcceptGreen
import com.example.guidemetravelersapp.ui.theme.CancelRed
import com.example.guidemetravelersapp.ui.theme.RecommendationOrange
import com.example.guidemetravelersapp.viewModels.ReservationViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@ExperimentalFoundationApi
@Composable
fun ShowReservationRequests(reservationViewModel: ReservationViewModel = viewModel()) {
    val isRefreshingReservationRequests by reservationViewModel.isRefreshingReservationRequests.collectAsState()

    reservationViewModel.getRequestReservationsForTourist()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshingReservationRequests),
        onRefresh = { reservationViewModel.refreshReservationRequests() },
        content = {
            LazyColumn(Modifier.fillMaxSize())  {
                item {
                    if(reservationViewModel.touristReservationRequests.data.isNullOrEmpty() && !reservationViewModel.touristReservationRequests.inProgress) {
                        Text(modifier = Modifier.padding(15.dp),text = stringResource(id = R.string.no_request))
                    }
                }
                if (reservationViewModel.touristReservationRequests.inProgress) {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LoadingSpinner()
                        }
                    }
                } else {
                    if(!reservationViewModel.touristReservationRequests.data.isNullOrEmpty()) {
                        itemsIndexed(reservationViewModel.touristReservationRequests.data!!) { index, item ->
                            Card(
                                modifier = Modifier.padding(15.dp),
                                elevation = 15.dp,
                                content = {
                                    ReservationRequestsCardContent(item)
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
fun ReservationRequestsCardContent(experienceReservation: ExperienceReservationRequest) {
    val from = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(experienceReservation.fromDate.time).atZone(
                ZoneId.systemDefault()).toLocalDate().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            ))
    ) }
    val to = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(experienceReservation.toDate.time).atZone(
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
                text = "${experienceReservation.guideFirstName} ${experienceReservation.guideLastName}",
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
                text = experienceReservation.address.city,
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
                text = experienceReservation.price.toString(),
                color = MaterialTheme.colors.onSecondary,
                style = TextStyle()
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Status(status = ReservationStatus.values()[experienceReservation.reservationStatus])
    }
}

@Composable
fun Status(status: ReservationStatus) {
    Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
    Spacer(modifier = Modifier.height(10.dp))
    when(status) {
        ReservationStatus.PENDING -> ReservationRequestStatus(
            color = RecommendationOrange,
            icon = Icons.Default.Warning,
            text = stringResource(id = R.string.pending)
        )
        ReservationStatus.ACCEPTED -> ReservationRequestStatus(
            color = AcceptGreen,
            icon = Icons.Default.CheckCircleOutline,
            text = stringResource(id = R.string.accepted)
        )
        ReservationStatus.REJECTED -> ReservationRequestStatus(
            color = CancelRed,
            icon = Icons.Default.DoNotDisturb,
            text = stringResource(id = R.string.rejected)
        )
    }
}

@Composable
fun ReservationRequestStatus(color: Color, icon: ImageVector, text: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Icon(imageVector = icon,
            contentDescription = null,
            tint = color)
        Text(
            text = text,
            color = color,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}
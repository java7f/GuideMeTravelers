package com.example.guidemetravelersapp.views.touristAlertsViews

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.helpers.commonComposables.AutoCompleteTextView
import com.example.guidemetravelersapp.viewModels.HomescreenViewModel
import com.example.guidemetravelersapp.viewModels.ReservationViewModel
import com.example.guidemetravelersapp.views.experienceDetailsView.reservationRequest.DateField
import java.time.Instant
import java.time.ZoneId
import java.util.*

@Composable
fun CreateTouristAlert(reservationViewModel: ReservationViewModel = viewModel()) {
    val from = remember { mutableStateOf(TextFieldValue(
        Instant.ofEpochMilli(Date().time).atZone(
            ZoneId.systemDefault()).toLocalDate().toString())) }
    val to = remember { mutableStateOf(TextFieldValue(
        Instant.ofEpochMilli(Date().time).atZone(
            ZoneId.systemDefault()).toLocalDate().toString())) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.create_new_alert),
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.select_trip_dates),
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(vertical = 25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.from_date) + ":",
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h6
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                DateField(from, reservationViewModel::updateFromDate)
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.to_date) + ":",
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h6
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                DateField(to, reservationViewModel::updateToDate)
            }
        }
    }
}

@Composable
fun SearchView(textState: MutableState<TextFieldValue>, modifier: Modifier, reservationViewModel: ReservationViewModel) {

    AutoCompleteTextView(
        modifier = modifier,
        query = textState.value.text,
        queryLabel = stringResource(id = R.string.search_by_location),
        onQueryChanged = { updateAddress ->
            textState.value = TextFieldValue(updateAddress)
            reservationViewModel.onQueryChanged(updateAddress)
        },
        predictions = reservationViewModel.predictions,
        onClearClick = {
            textState.value = TextFieldValue("")
            reservationViewModel.locationSearchValue = ""
            reservationViewModel.predictions = mutableListOf()
        },
        onItemClick = { place ->
            textState.value = TextFieldValue(place.getPrimaryText(null).toString())
            reservationViewModel.onPlaceItemSelected(place)
        }
    ) {
        Text("${it.getFullText(null)}", style = MaterialTheme.typography.caption)
    }
}
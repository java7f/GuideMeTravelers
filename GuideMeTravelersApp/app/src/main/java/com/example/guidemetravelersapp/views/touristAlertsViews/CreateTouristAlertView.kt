package com.example.guidemetravelersapp.views.touristAlertsViews

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.helpers.commonComposables.AutoCompleteTextView
import com.example.guidemetravelersapp.viewModels.HomescreenViewModel
import com.example.guidemetravelersapp.viewModels.ReservationViewModel
import com.example.guidemetravelersapp.views.experienceDetailsView.DescriptionTags
import com.example.guidemetravelersapp.views.experienceDetailsView.reservationRequest.DateField
import java.time.Instant
import java.time.ZoneId
import java.util.*

@Composable
fun CreateTouristAlert(navHostController: NavHostController, reservationViewModel: ReservationViewModel = viewModel()) {
    val context = LocalContext.current
    val from = remember { mutableStateOf(TextFieldValue(
        Instant.ofEpochMilli(Date().time).atZone(
            ZoneId.systemDefault()).toLocalDate().toString())) }
    val to = remember { mutableStateOf(TextFieldValue(
        Instant.ofEpochMilli(Date().time).atZone(
            ZoneId.systemDefault()).toLocalDate().toString())) }
    val comment = remember { mutableStateOf(TextFieldValue(text = reservationViewModel.currentTouristAlert.alertComment)) }
    val focusManager = LocalFocusManager.current
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    val tag = remember { mutableStateOf(TextFieldValue(text = "")) }
    val tags = remember { mutableStateOf(reservationViewModel.currentTouristAlert.experienceTags) }
    tags.value = reservationViewModel.currentTouristAlert.experienceTags

    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
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
                text = stringResource(id = R.string.tourist_alert_location),
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
        }
        SearchView(
            textState,
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            reservationViewModel = reservationViewModel
        )

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
                style = MaterialTheme.typography.subtitle2
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                DateField(from, reservationViewModel::updateFromDateTouristAlert)
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.to_date) + ":",
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.subtitle2
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                DateField(to, reservationViewModel::updateToDateTouristAlert)
            }
        }

        Row(
            modifier = Modifier
                .padding(vertical = 25.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.write_comment),
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = comment.value,
                onValueChange = { value ->
                    comment.value = value
                    reservationViewModel.currentTouristAlert.alertComment = value.text
                },
                label = { Text(text = stringResource(id = R.string.write_comment)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                maxLines = 5
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            for (tagItem in tags.value) {
                DescriptionTags(tagName = tagItem)
            }
        }

        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = tag.value,
                onValueChange = { value ->
                    tag.value = value
                },
                label = { Text(text = stringResource(id = R.string.add_tag)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                maxLines = 1,
                trailingIcon = {
                    TextButton(onClick = {
                        tags.value.add(tag.value.text)
                        tag.value = TextFieldValue("")
                        focusManager.clearFocus()
                    }) {
                        Text(
                            text = stringResource(id = R.string.add_tag),
                            color = MaterialTheme.colors.onSecondary,
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {

            OutlinedButton(
                onClick = { navHostController.popBackStack() },
            ) {
                Text(
                    stringResource(id = R.string.cancel),
                    color = MaterialTheme.colors.onError,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }

            Button(
                onClick = {
                    reservationViewModel.insertTouristAlert(navHostController, tags.value)
                    if (!reservationViewModel.newTouristAlertStatus.hasError) {
                        Toast.makeText(context, "Alert created successfully!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, reservationViewModel.newTouristAlertStatus.errorMessage, Toast.LENGTH_LONG).show()
                    } },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary
                ),
                enabled = !reservationViewModel.newTouristAlertStatus.inProgress,
            ) {
                Text(
                    stringResource(id = R.string.confirm_request),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                if(reservationViewModel.newTouristAlertStatus.inProgress) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(22.dp)
                    )
                }
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
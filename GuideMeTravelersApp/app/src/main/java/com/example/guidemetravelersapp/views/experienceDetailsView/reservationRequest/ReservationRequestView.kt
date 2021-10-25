package com.example.guidemetravelersapp.views.experienceDetailsView.reservationRequest

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.ExperienceReservationRequest
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingBar
import com.example.guidemetravelersapp.helpers.models.ScreenStateEnum
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.viewModels.ReservationViewModel
import com.example.guidemetravelersapp.views.profileView.ReadonlyTextField
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ReservationRequestContent(experienceId: String = "", navHostController: NavHostController, reservationViewModel: ReservationViewModel = viewModel()) {
    reservationViewModel.initCurrentReservationRequest(experienceId = experienceId)
    val from = remember { mutableStateOf(TextFieldValue(Instant.ofEpochMilli(Date().time).atZone(ZoneId.systemDefault()).toLocalDate().toString())) }
    val to = remember { mutableStateOf(TextFieldValue(Instant.ofEpochMilli(Date().time).atZone(ZoneId.systemDefault()).toLocalDate().toString())) }
    GuideMeTravelersAppTheme {
        if(reservationViewModel.initReservationRequestStatus.inProgress) {
            Column(verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth()) {
                LoadingBar()
            }
        }
        else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(15.dp)) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(id = R.string.reservation_with)+ " ${reservationViewModel.currentReservationRequest.guideFirstName} ${reservationViewModel.currentReservationRequest.guideLastName}",
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.destination) + ": ",
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.h6,
                    )
                    Text(
                        text = "Santo Domingo",
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.h6,
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
                        onClick = { reservationViewModel.insertReservationRequest(navHostController) },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary
                        ),
                        enabled = !reservationViewModel.newReservationRequestStatus.inProgress,
                    ) {
                        Text(
                            stringResource(id = R.string.confirm_request),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                        if(reservationViewModel.newReservationRequestStatus.inProgress) {
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
    }
}

@Composable
fun DateField(dateField: MutableState<TextFieldValue>, updateDate: (newDate: Date) -> Unit) {
    val dialog = MaterialDialog()

    dialog.build(
        buttons = {
            positiveButton(text = stringResource(id = R.string.ok))
            negativeButton(text = stringResource(id = R.string.cancel))
        },
        content = {
            datepicker(
                colors = DatePickerDefaults.colors(
                    headerTextColor = MaterialTheme.colors.onSecondary,
                    activeTextColor = Color.White
                )
            ) { date ->
                val formattedDate = date.format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
                dateField.value = TextFieldValue(formattedDate)
                updateDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
            }
        }
    )
    Column(
        content = {
            ReadonlyTextField(
                value = dateField.value,
                onValueChange = { dateField.value = it },
                modifier = Modifier.width(260.dp),
                onClick = { dialog.show() },
                label = {
                    Text(
                        text = stringResource(id = R.string.from_to),
                    )
                }
            )
        }
    )
}
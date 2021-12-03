package com.example.guidemetravelersapp.views.touristAlertsViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.GuidingOffer
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.example.guidemetravelersapp.viewModels.ReservationViewModel
import com.skydoves.landscapist.coil.CoilImage
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun GuidingOffers(navController: NavHostController, reservationViewModel: ReservationViewModel = viewModel()) {
    reservationViewModel.getGuideOffersForTourist()
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(20.dp))  {
        if (reservationViewModel.guideOffersForTourist.inProgress) {
            item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingSpinner()
                }
            }
        } else {
            if(!reservationViewModel.guideOffersForTourist.data.isNullOrEmpty()) {
                itemsIndexed(reservationViewModel.guideOffersForTourist.data!!) { index, item ->
                    Card(
                        elevation = 15.dp,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = MaterialTheme.colors.secondary),
                        onClick = { navController.navigate("guideExperience/${item.guideExperienceId}") },
                        content = {
                            GuideOfferCardContent(item, reservationViewModel)
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GuideOfferCardContent(guideOffer: GuidingOffer, reservationViewModel: ReservationViewModel) {
    val from = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(guideOffer.fromDate.time).atZone(
                ZoneId.systemDefault()).toLocalDate().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            ))
    ) }
    val to = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(guideOffer.toDate.time).atZone(
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    if(guideOffer.guidePhotoUrl.isEmpty()) {
                        Image(
                            painter = painterResource(R.drawable.dummy_avatar),
                            contentDescription = "Temporal dummy avatar",
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(70.dp)
                        )
                    } else {
                        Box(modifier = Modifier
                            .size(70.dp)
                            .border(
                                2.dp,
                                MaterialTheme.colors.secondary,
                                CircleShape
                            )) {
                            CoilImage(imageModel = guideOffer.guidePhotoUrl,
                                contentDescription = "User profile photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.clip(CircleShape))
                        }
                    }
                    Column(Modifier.padding(start = 10.dp)) {
                        Text(
                            text = stringResource(id = R.string.guide_name) +": ",
                            color = MaterialTheme.colors.onSecondary,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${guideOffer.guideFirstName} ${guideOffer.guideLastName}",
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.destination)+": ",
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = guideOffer.touristDestination,
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

        Spacer(modifier = Modifier.height(20.dp))
        AcceptRejectGuideOffer(guideOffer = guideOffer, reservationViewModel = reservationViewModel)
    }
}

@Composable
fun AcceptRejectGuideOffer(guideOffer: GuidingOffer, reservationViewModel: ReservationViewModel) {
    Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
    ) {

        OutlinedButton(
            onClick = { reservationViewModel.rejectGuideOffer(guideOffer.id) },
            enabled = !reservationViewModel.rejectGuideOffer.inProgress,
        ) {
            Text(
                stringResource(id = R.string.rejet_request),
                color = MaterialTheme.colors.onError,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            if(reservationViewModel.rejectGuideOffer.inProgress) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(22.dp)
                )
            }
        }

        Button(
            onClick = { reservationViewModel.acceptGuideOffer(guideOffer.id) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            enabled = !reservationViewModel.acceptGuideOffer.inProgress,
        ) {
            Text(
                stringResource(id = R.string.accept_request),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            if(reservationViewModel.acceptGuideOffer.inProgress) {
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
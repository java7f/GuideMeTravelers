package com.example.guidemetravelersapp.views.experienceHistoryView

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.ExperienceReservation
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.viewModels.ReservationViewModel

class ExperienceHistoryActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeTravelersAppTheme {
                ShowPastExperiences()
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ShowPastExperiences(reservationViewModel: ReservationViewModel = viewModel()) {
    LazyColumn(Modifier.fillMaxSize())  {
        stickyHeader {
            Text(
                text = stringResource(id = R.string.past_experiences),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .padding(vertical = 25.dp)
            )
        }
        if(!reservationViewModel.pastExperienceReservations.data.isNullOrEmpty()) {
            itemsIndexed(reservationViewModel.pastExperienceReservations.data!!) { index, item ->
                Card(
                    modifier = Modifier.padding(15.dp),
                    elevation = 15.dp,
                    content = {
                        PastExperienceCardContent(item)
                    }
                )
            }
        }
    }
}

@Composable
fun PastExperienceCardContent(experiencieReservation: ExperienceReservation) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.guide_name)+": ",
                color = MaterialTheme.colors.onSecondary,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
            Text(
                text = "${experiencieReservation.guideFirstName} ${experiencieReservation.guideLastName}",
                color = MaterialTheme.colors.primaryVariant,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.destination)+": ",
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = "",
                color = MaterialTheme.colors.onSecondary,
                style = TextStyle()
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.from_to)+": ",
                color = MaterialTheme.colors.onSecondary
            )
            //TODO: add dates from backend

        }
        Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.price)+": ",
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = experiencieReservation.price.toString(),
                color = MaterialTheme.colors.onSecondary,
                style = TextStyle()
            )
        }
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileHistoryPreview() {
    GuideMeTravelersAppTheme {
        ShowPastExperiences()
    }
}
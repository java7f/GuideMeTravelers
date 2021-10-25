package com.example.guidemetravelersapp.views.experienceDetailsView

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.dataModels.GuideExperience
import com.example.guidemetravelersapp.dataModels.Review
import com.example.guidemetravelersapp.ui.theme.Gray200
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.ui.theme.MilitaryGreen200
import com.example.guidemetravelersapp.viewModels.GuideExperienceViewModel
import com.example.guidemetravelersapp.viewModels.HomescreenViewModel
import com.gowtham.ratingbar.RatingBar
import com.skydoves.landscapist.coil.CoilImage

class ExperienceDetailsActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeTravelersAppTheme {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()) {
                    GuideDescriptionExperience()
                }
            }
        }
    }

    fun getViewModel(): GuideExperienceViewModel {
        val model: GuideExperienceViewModel by viewModels()
        return model
    }
}

@ExperimentalFoundationApi
@Composable
fun GuideDescriptionExperience(
    experienceId: String = "",
    navHostController: NavHostController? = null,
    model: GuideExperienceViewModel = viewModel()
) {
    model.experienceId = experienceId
    model.updateExperience()
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()) {
        item {
            Spacer(modifier = Modifier.height(30.dp))

            GuideInfo(model.guideExperience)

            //Rating stars for the guide
            Spacer(modifier = Modifier.height(15.dp))
            GuideRating(model.guideExperience.guideRating)

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Gray200, thickness = 1.dp)
            Spacer(modifier = Modifier.height(15.dp))

            Reservation(model.guideExperience.experiencePrice, model.guideExperience.guideFirebaseId ?: "", experienceId, navHostController!!)

            Spacer(modifier = Modifier.height(15.dp))
            Divider(color = Gray200, thickness = 1.dp)
            Spacer(modifier = Modifier.height(15.dp))

            Description(model.guideExperience.experienceDescription)

            Spacer(modifier = Modifier.height(15.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)) {
                model.guideExperience.experienceTags.forEach { tag ->
                    DescriptionTags(tagName = tag)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Gray200, thickness = 1.dp)
            Spacer(modifier = Modifier.height(15.dp))
        }

        stickyHeader {
            TouristExperienceRating(model.guideExperience.guideReviews.size)
        }

        itemsIndexed(model.guideExperience.guideReviews) { index, item ->
            TouristReview(item)
        }
    }
}

@Composable
fun GuideRating(userRating: Float) {
    RatingBar(value = userRating, size = 20.dp, isIndicator = true) { }
}

@Composable
fun GuideInfo(guideExperience: GuideExperience) {
        if(guideExperience.guidePhotoUrl.isNullOrEmpty()) {
            Box(modifier = Modifier.size(120.dp)) {
                Image(
                    painter = painterResource(R.drawable.dummy_avatar),
                    contentDescription = "Temporal dummy avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape)
                )
            }
        }
        else {
            Box(modifier = Modifier.size(120.dp)) {
                CoilImage(
                    imageModel = guideExperience.guidePhotoUrl,
                    contentDescription = "Guide photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape)
                )
            }
        }

    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "${guideExperience.guideFirstName} ${guideExperience.guideLastName}",
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSecondary,
        fontSize = 25.sp
    )
}

@Composable
fun Description(experienceDescription: String) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()){
        Text(
            text = stringResource(id = R.string.experience_description_text),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(start = 15.dp)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = experienceDescription,
        color = MaterialTheme.colors.onPrimary,
        modifier = Modifier.padding(horizontal = 15.dp)
    )
}

@Composable
fun Reservation(experiencePrice: Float, guideId: String, guideExperienceId: String, navHostController: NavHostController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = experiencePrice.toString(),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(start = 25.dp)
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Outlined.Chat,
                contentDescription = null, tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(end = 15.dp)
                    .clickable {
                        navHostController.navigate("chat_with/$guideId")
                    }
            )

            Button(
                onClick = { navHostController.navigate("request_reservation/$guideExperienceId") },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                modifier = Modifier.padding(end = 25.dp)
            ) {
                Icon(imageVector = Icons.Filled.DateRange , contentDescription = null,
                    tint = Color.White, modifier = Modifier.padding(end = 10.dp))
                Text(text = stringResource(id = R.string.reserve_text), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DescriptionTags(tagName: String) {
    Box(modifier = Modifier.padding(end = 10.dp)) {
        Text(
            text = tagName,
            modifier = Modifier
                .background(
                    color = MilitaryGreen200,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            overflow = TextOverflow.Ellipsis,
            color = Color.White,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun TouristExperienceRating(reviewsCount: Int) {
    Row(horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.reviews_text),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(start = 15.dp)
        )
        Text(
            text = "($reviewsCount)",
            fontSize = 10.sp,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
fun TouristReview(touristReview: Review) {
    Row(verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.padding(15.dp)) {
        Box(modifier = Modifier.size(50.dp)) {
            Image(
                painter = painterResource(R.drawable.dummy_avatar),
                contentDescription = "Temporal dummy avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
        Column(modifier = Modifier.padding(start = 15.dp)) {
            Text(
                text = touristReview.userName,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 15.sp
            )
            RatingBar(value = touristReview.ratingValue, size = 15.dp, isIndicator = true) {
            }
            Text(text = touristReview.ratingComment,
                color = MaterialTheme.colors.onPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true,
    showSystemUi = true,
    locale = "es")
@Composable
fun GuideExperienceDetailPreview() {
    GuideMeTravelersAppTheme {
        GuideDescriptionExperience()
    }
}
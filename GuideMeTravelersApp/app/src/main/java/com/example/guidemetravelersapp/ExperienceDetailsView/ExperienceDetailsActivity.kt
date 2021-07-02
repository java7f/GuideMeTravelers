package com.example.guidemetravelersapp.ExperienceDetailsView

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
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
import com.example.guidemetravelersapp.ui.theme.Gray200
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.ui.theme.MilitaryGreen200
import com.gowtham.ratingbar.RatingBar

class ExperienceDetailsActivity : ComponentActivity() {
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
}

@Composable
fun GuideDescriptionExperience() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(30.dp))
        
        GuideInfo()

        //Rating stars for the guide
        Spacer(modifier = Modifier.height(15.dp))
        GuideRating(userRating = 3.5f)
        
        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Gray200, thickness = 1.dp)
        Spacer(modifier = Modifier.height(15.dp))
        
        Reservation()

        Spacer(modifier = Modifier.height(15.dp))
        Divider(color = Gray200, thickness = 1.dp)
        Spacer(modifier = Modifier.height(15.dp))

        Description()

        Spacer(modifier = Modifier.height(15.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DescriptionTags(tagName = "culture")
            DescriptionTags(tagName = "gastronomy")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Gray200, thickness = 1.dp)
        Spacer(modifier = Modifier.height(15.dp))

        TouristExperienceRating()
        TouristReview(touristRating = 3.7f)
        TouristReview(touristRating = 4.7f)
    }
}

@Composable
fun GuideRating(userRating: Float) {
    RatingBar(value = userRating, size = 20.dp, isIndicator = true) {
    }
}

@Composable
fun GuideInfo() {
    Box(modifier = Modifier.size(120.dp)) {
        Image(
            painter = painterResource(R.drawable.dummy_avatar),
            contentDescription = "Temporal dummy avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape)
        )
    }

    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "Name Lastname",
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onPrimary,
        fontSize = 25.sp
    )
}

@Composable
fun Description() {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()){
        Text(
            text = stringResource(id = R.string.experience_description_text),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(start = 15.dp)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = stringResource(id = R.string.loremipsum_test),
        color = MaterialTheme.colors.onPrimary,
        modifier = Modifier.padding(horizontal = 15.dp)
    )
}

@Composable
fun Reservation() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "20$",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(start = 25.dp)
        )

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier.padding(end = 25.dp)
        ) {
            Icon(imageVector = Icons.Filled.DateRange , contentDescription = null, tint = Color.White)
            Text(text = stringResource(id = R.string.reserve_text), color = Color.White)
        }
    }
}

@Composable
fun DescriptionTags(tagName: String) {
    Box(modifier = Modifier.padding(start = 10.dp)) {
        Text(
            text = tagName,
            modifier = Modifier
                .background(
                    color = MilitaryGreen200,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}

@Composable
fun TouristExperienceRating() {
    Row(horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.reviews_text),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(start = 15.dp)
        )
        Text(
            text = "(12)",
            fontSize = 10.sp,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
fun TouristReview(touristRating: Float) {
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
                text = "Name Lastname",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 15.sp
            )
            RatingBar(value = touristRating, size = 15.dp, isIndicator = true) {
            }
            Text(text = stringResource(id = R.string.loremipsum_test),
                color = MaterialTheme.colors.onPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview3() {
    GuideMeTravelersAppTheme {
        GuideDescriptionExperience()
    }
}
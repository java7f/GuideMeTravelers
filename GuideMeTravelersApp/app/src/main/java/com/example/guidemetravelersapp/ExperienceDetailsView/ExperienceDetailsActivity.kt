package com.example.guidemetravelersapp.ExperienceDetailsView

import android.os.Bundle
import android.widget.RatingBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.R
import androidx.compose.ui.text.font.FontStyle
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ProfileInfo()
                }
            }
        }
    }
}

@Composable
fun ProfileInfo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(40.dp))
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

        //Rating stars for the guide
        Spacer(modifier = Modifier.height(15.dp))
        RatingBar(value = 3.2f, size = 20.dp, isIndicator = true) {
            
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Gray200, thickness = 1.dp)
        Spacer(modifier = Modifier.height(15.dp))
        
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

        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Gray200, thickness = 1.dp)
        Spacer(modifier = Modifier.height(15.dp))

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
        Spacer(modifier = Modifier.height(15.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = "culture",
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
            Box() {
                Text(
                    text = "gastronomy",
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

        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = Gray200, thickness = 1.dp)
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview3() {
    GuideMeTravelersAppTheme {
        ProfileInfo()
    }
}
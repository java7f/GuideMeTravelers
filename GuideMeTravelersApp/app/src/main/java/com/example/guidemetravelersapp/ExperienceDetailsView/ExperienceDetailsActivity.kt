package com.example.guidemetravelersapp.ExperienceDetailsView

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.R
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme

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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.size(90.dp)) {
            Image(
                painter = painterResource(R.drawable.dummy_avatar),
                contentDescription = "Temporal dummy avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Name Lastname", fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onPrimary)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview3() {
    GuideMeTravelersAppTheme {
        ProfileInfo()
    }
}
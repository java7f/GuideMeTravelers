package com.example.guidemetravelersapp.helpers.commonComposables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun FullsizeImage(imageUrl: String) {
    Column(Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CoilImage(imageModel = imageUrl,
                contentDescription = "User profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(RectangleShape))
        }
    }
}
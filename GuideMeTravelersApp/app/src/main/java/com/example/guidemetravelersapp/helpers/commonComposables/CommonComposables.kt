package com.example.guidemetravelersapp.helpers.commonComposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun FullsizeImage(imageUrl: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        CoilImage(imageModel = imageUrl,
            contentDescription = "User profile photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(RectangleShape))
    }
}
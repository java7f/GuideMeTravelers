package com.example.guidemetravelersapp.helpers.commonComposables

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.helpers.LanguageManager
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

@Composable
fun LoadingBar() {
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
}

@Composable
fun Dropdown(
    items: List<Any>,
    modifier: Modifier,
    action: (selectedOption: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = !expanded },
        modifier = modifier
    ) {
        items.forEachIndexed { index, item ->
            DropdownMenuItem(onClick = {
                action(item as String)
                expanded = false
            }) {
                Text(text = item as String)
            }
        }
    }
}
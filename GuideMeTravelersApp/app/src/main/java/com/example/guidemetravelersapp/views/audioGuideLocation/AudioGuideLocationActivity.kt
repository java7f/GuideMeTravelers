package com.example.guidemetravelersapp.views.audioGuideLocation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AudioGuideLocationActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeTravelersAppTheme {
                LocationContent()
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun LocationContent() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetContent = {
            VideoPlayer()
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = true,
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            content = {
                                Box(
                                    content = {
                                        Image(
                                            painter = painterResource(R.drawable.alcazar_de_colon),
                                            contentDescription = "Alcazar de colon",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(250.dp)
                                        )
                                    }
                                )
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            content = {
                                Text(
                                    text = "Alcázar de Colón",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.onSecondary,
                                    fontSize = 25.sp
                                )
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            content = {
                                Row(
                                    content = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.placeholder),
                                            contentDescription = "Location",
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(30.dp)
                                        )
                                        Text(
                                            text = "Santo Domingo",
                                            fontSize = 18.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 5.dp, start = 8.dp),
                                            color = MaterialTheme.colors.onSecondary
                                        )
                                    }
                                )
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            content = {
                                Text(
                                    text = "Audio guides",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colors.onBackground,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Location",
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        )
                        Divider(
                            color = MaterialTheme.colors.onBackground,
                            thickness = 2.dp,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        LocationCard(title = "Bunny Test", scaffoldState, scope)
                        Spacer(modifier = Modifier.height(15.dp))
                        LocationCard(title = "Test", scaffoldState, scope)
                    }
                }
            )
        }
    )
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun AudioGuideLocationPreview() {
    GuideMeTravelersAppTheme {
        LocationContent()
    }
}

@ExperimentalMaterialApi
@Composable
fun LocationCard(title: String, scaffoldState: BottomSheetScaffoldState, scope: CoroutineScope) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) scaffoldState.bottomSheetState.expand()
                        else scaffoldState.bottomSheetState.collapse() }
                }
            ),
        elevation = 10.dp,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Play audio guide",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colors.secondary
                    )
                    Column(modifier = Modifier.padding(start = 20.dp)) {
                        Text(
                            text = title,
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(text = "2 min")
                    }
                }
            )
        }
    )
}

@Composable
fun VideoPlayer() {
    val context = LocalContext.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember(context) {
        SimpleExoPlayer.Builder(context).build().apply {
            val videoUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3\n"
            val mediaItem = MediaItem.fromUri(videoUrl)
            val mediaSource = DefaultMediaSourceFactory(context).createMediaSource(mediaItem)
            setMediaSource(mediaSource)
            prepare()
        }
    }

    // Gateway to traditional Android Views
    AndroidView(modifier = Modifier.height(150.dp).padding(bottom = 20.dp, top = 20.dp), factory = {
        PlayerView(context).apply {
            player = exoPlayer
            controllerAutoShow = true
            controllerHideOnTouch = false
        }
    })
}
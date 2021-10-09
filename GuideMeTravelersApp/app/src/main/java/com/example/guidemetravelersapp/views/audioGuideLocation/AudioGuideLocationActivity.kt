package com.example.guidemetravelersapp.views.audioGuideLocation

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.Audioguide
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.viewModels.LocationViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.skydoves.landscapist.coil.CoilImage
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
fun LocationContent(locationId: String = "", model: LocationViewModel = viewModel()) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    model.getLocation(locationId = locationId)
    model.getAudioguidesForLocation(locationId = locationId)

    BottomSheetScaffold(
        sheetContent = {
            VideoPlayer(model.currentAudioguideUrl)
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
                                    modifier = Modifier.fillMaxWidth(),
                                    content = {
                                        if(model.currentLocation.data?.locationPhotoUrl!!.isEmpty()) {
                                            Image(
                                                painter = painterResource(R.drawable.dummy_avatar),
                                                contentDescription = "Temporal dummy avatar",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(250.dp)
                                            )
                                        } else {
                                            Box(modifier = Modifier
                                                .fillMaxWidth()) {
                                                CoilImage(
                                                    imageModel = model.currentLocation.data?.locationPhotoUrl!!,
                                                    contentDescription = "Location photo",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(250.dp)
                                                )
                                            }
                                        }
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
                                    text = model.currentLocation.data?.name!!,
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
                                    contentDescription = "Download audioguides",
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
                    }
                    itemsIndexed(model.audioguides.data!!) { index, item ->
                        Spacer(modifier = Modifier.height(20.dp))
                        LocationCard(item, scaffoldState, scope, model)
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
fun LocationCard(audioguide: Audioguide, scaffoldState: BottomSheetScaffoldState,
                 scope: CoroutineScope, model: LocationViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = {
                    model.currentAudioguideUrl = audioguide.audioguideUrl
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) scaffoldState.bottomSheetState.expand()
                        else scaffoldState.bottomSheetState.collapse()
                    }
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
                            text = audioguide.name,
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
fun VideoPlayer(audioUrl: String) {
    val context = LocalContext.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    LaunchedEffect(audioUrl) {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
            Util.getUserAgent(context, context.packageName))

        val mediaItem = MediaItem.fromUri(audioUrl)
        val mediaSource = DefaultMediaSourceFactory(context).createMediaSource(mediaItem)

        exoPlayer.prepare(mediaSource)
    }

    // Gateway to traditional Android Views
    AndroidView(modifier = Modifier
        .height(150.dp)
        .padding(bottom = 20.dp, top = 20.dp), factory = {
        PlayerView(context).apply {
            player = exoPlayer
            controllerAutoShow = true
            controllerHideOnTouch = false
        }
    })
}
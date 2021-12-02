package com.example.guidemetravelersapp.views.audioGuideLocation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.Audioguide
import com.example.guidemetravelersapp.helpers.RoutineManager
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingBar
import com.example.guidemetravelersapp.ui.theme.AcceptGreen
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.ui.theme.RecommendationOrange
import com.example.guidemetravelersapp.ui.theme.Teal200
import com.example.guidemetravelersapp.viewModels.LocationViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class AudioGuideLocationActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
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

@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalMaterialApi
@Composable
fun LocationContent(locationId: String = "", model: LocationViewModel = viewModel()) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    model.getLocation(locationId = locationId)
    model.getAudioguidesForLocation(locationId = locationId)
    model.registerScanRoutine()

    DisposableEffect(LocalLifecycleOwner.current) {
        onDispose {
            RoutineManager.cancelRoutines()
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            VideoPlayer(model.currentAudioguideUrl)
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = true,
        content = {
            Box(Modifier.padding(it.calculateBottomPadding())){
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
                                            if (model.currentLocation.data?.locationPhotoUrl!!.isEmpty() && !model.getOfflineModeStatus()) {
                                                Image(
                                                    painter = painterResource(R.drawable.dummy_avatar),
                                                    contentDescription = "Temporal dummy avatar",
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(250.dp)
                                                )
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                ) {
                                                    CoilImage(
                                                        imageModel = if(model.getOfflineModeStatus()) Uri.fromFile(
                                                            File(model.currentLocation.data!!.locationOfflinePath)
                                                        ) else model.currentLocation.data?.locationPhotoUrl!!,
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
                                    .padding(vertical = 20.dp, horizontal = 15.dp),
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
                                    .padding(bottom = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                content = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        content = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.placeholder),
                                                contentDescription = "Location",
                                                tint = Color.Unspecified,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = "${model.currentLocation.data!!.address.city}, ${model.currentLocation.data!!.address.country}",
                                                style = MaterialTheme.typography.subtitle2,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(
                                                    top = 5.dp,
                                                    start = 8.dp
                                                ),
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
                                        text = stringResource(id = R.string.audioguides),
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colors.onBackground,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    IconButton(onClick = {
                                        model.downloadFile()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.DownloadForOffline,
                                            contentDescription = "Download audioguides",
                                            tint = MaterialTheme.colors.onBackground,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                }
                            )
                            Divider(
                                color = MaterialTheme.colors.onBackground,
                                thickness = 2.dp,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            if (model.isLoadingDecrypting)
                                LoadingBar()
                        }
                        if(!model.proximityRecommendedAudioguides.data.isNullOrEmpty()) {
                            item {
                                Text(
                                    text = stringResource(id = R.string.near_you),
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colors.onBackground,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
                                )
                            }
                            itemsIndexed(model.proximityRecommendedAudioguides.data!!) { index, item ->
                                Box(modifier = Modifier.border(2.dp, RecommendationOrange, RoundedCornerShape(8.dp))) {
                                    LocationCard(item, scaffoldState, scope, model)
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                        item {
                            Text(
                                text = stringResource(id = R.string.all_exhibitions),
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.onBackground,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
                            )
                        }
                        itemsIndexed(model.audioguides.data!!) { index, item ->
                            //model.isAudioguideDownloaded(item.id)
                            LocationCard(item, scaffoldState, scope, model)
                        }
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun AudioGuideLocationPreview() {
    GuideMeTravelersAppTheme {
        LocationContent()
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalMaterialApi
@Composable
fun LocationCard(audioguide: Audioguide, scaffoldState: BottomSheetScaffoldState,
                 scope: CoroutineScope, model: LocationViewModel) {
    val isDownloaded = remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = {
                    if (!model.getOfflineModeStatus())
                        model.playAudio(audioguide.audioguideUrl)
                    else
                        model.playAudio(audioguide.audiofileName)
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) scaffoldState.bottomSheetState.expand()
                        else scaffoldState.bottomSheetState.collapse()
                    }
                }
            ),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(20.dp),
                content = {
                    scope.launch { isDownloaded.value = model.isAudioguideDownloaded(audioId = audioguide.id) }
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Play audio guide",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colors.secondary
                    )
                    Column(modifier = Modifier.padding(start = 20.dp).weight(1f)) {
                        Text(
                            text = audioguide.name,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if(isDownloaded.value)
                        Icon(
                            imageVector = Icons.Default.DownloadDone,
                            contentDescription = "IsDownloaded",
                            tint = AcceptGreen
                        )
                    if(audioguide.id == model.currentEncryptingAudio.id){
                        CircularProgressIndicator(
                            progress = model.currentAudioguideDownloadProgress,
                            color = AcceptGreen,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
            Divider(Modifier.fillMaxWidth())
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
        .height(100.dp), factory = {
        PlayerView(context).apply {
            player = exoPlayer
            controllerAutoShow = true
            controllerHideOnTouch = false
            controllerShowTimeoutMs = PlayerControlView.KEEP_SCREEN_ON
            useArtwork = false
        }
    })
}
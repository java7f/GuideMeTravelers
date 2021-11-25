package com.example.guidemetravelersapp.views.wishlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdded
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.GuideExperience
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.viewModels.GuideExperienceViewModel
import com.example.guidemetravelersapp.viewModels.ProfileViewModel
import com.example.guidemetravelersapp.views.experienceDetailsView.DescriptionTags
import com.example.guidemetravelersapp.views.experienceDetailsView.GuideRating
import com.skydoves.landscapist.coil.CoilImage

class WishlistActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeTravelersAppTheme {
                WishlistContent()
            }
        }
    }
}

@Composable
fun WishlistContent(
    experienceModel: GuideExperienceViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    navHostController: NavHostController? = null
) {
    experienceModel.userId = profileViewModel.editableUser.firebaseUserId
    experienceModel.getExperiencesByUserId()

    LazyColumn(
        modifier = Modifier
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .fillMaxSize(),
        content = {
            item {
                Text(
                    text = stringResource(id = R.string.possible_guides),
                    color = MaterialTheme.colors.onSecondary,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                if(experienceModel.userGuideExps.data.isNullOrEmpty()) {
                    Text(text = stringResource(id = R.string.no_guides_wishlist))
                }
            }
            if(!experienceModel.userGuideExps.data.isNullOrEmpty()) {
                itemsIndexed(experienceModel.userGuideExps.data!!) {index, item ->
                    UserCard(
                        name = item.guideFirstName,
                        lastname = item.guideLastName,
                        imageUrl = item.guidePhotoUrl,
                        imgSize = 70.dp,
                        rating = item.guideRating,
                        tags = item.experienceTags,
                        experienceId = item.id,
                        navController = navHostController!!,
                        profileViewModel = profileViewModel
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    )
}

@Composable
fun UserCard(
    name: String,
    lastname: String,
    imageUrl: String,
    imgSize: Dp,
    rating: Float,
    tags: List<String>,
    experienceId: String,
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = { navController.navigate("guideExperience/${experienceId}") }
            ),
        elevation = 5.dp,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
                if(imageUrl.isEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.dummy_avatar),
                        contentDescription = "Temporal dummy avatar",
                        modifier = Modifier
                            .clip(CircleShape)
                            .height(imgSize)
                    )
                }
                else {
                    Box(modifier = Modifier
                        .size(imgSize)) {
                        CoilImage(
                            imageModel = imageUrl,
                            contentDescription = "Guide photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.clip(CircleShape)
                        )
                    }
                }
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween,
                        content = {
                            Text(
                                text = "$name $lastname",
                                style = MaterialTheme.typography.subtitle1,
                                fontWeight = FontWeight.Bold)
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                tint = MaterialTheme.colors.error,
                                contentDescription = "remove guide",
                                modifier = Modifier.clickable {
                                    profileViewModel.editableUser.wishlist.remove(experienceId)
                                    profileViewModel.saveProfileChange(null)
                                    // refreshing wishlist
                                    navController.navigate("wishlist")
                                })
                    })
                    GuideRating(rating)
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)) {
                        for (tag in tags) {
                            DescriptionTags(tagName = tag)
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun WishlistPreview() {
    GuideMeTravelersAppTheme {
        WishlistContent()
    }
}
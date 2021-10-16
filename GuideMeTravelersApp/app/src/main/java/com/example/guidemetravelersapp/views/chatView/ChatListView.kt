package com.example.guidemetravelersapp.views.chatView

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.User
import com.example.guidemetravelersapp.viewModels.ChatViewModel

@Composable
fun ChatList(
        navController: NavHostController,
        chatViewModel: ChatViewModel = viewModel()
) {
    val listState = rememberLazyListState()
    LazyColumn(modifier = Modifier
        .fillMaxSize(),
        state = listState
    ) {
        item {
            Text(
                text = stringResource(id = R.string.chat),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(bottom = 20.dp).padding(15.dp),
                fontWeight = FontWeight.Bold
            )
        }
        itemsIndexed(chatViewModel.currentUserChannelList.keys.toList()) { index, item ->
            chatViewModel.currentUserChannelList.get(item)?.let {
                ChatCard(user = it,navController = navController)
            }
        }
    }
//    else {
//        Column(Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(text = "No chats", style = MaterialTheme.typography.h6)
//        }
//    }
}

/* User card with standard user information, excluding username and adding rating and tags */
@Composable
fun ChatCard(user: User, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = { navController.navigate("chat_with/${user.firebaseUserId}") }
            ),
        content = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
                Image(
                    painter = painterResource(R.drawable.dummy_avatar),
                    contentDescription = "Temporal dummy avatar",
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(50.dp)
                )
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
            Divider(Modifier.fillMaxWidth())
        }
    )
}
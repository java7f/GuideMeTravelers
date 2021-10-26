package com.example.guidemetravelersapp.views.chatView

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemetravelersapp.dataModels.chat.Message
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.viewModels.ChatViewModel

class ChatActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeTravelersAppTheme {
                ChatView()
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ChatView(sentTo_Id: String = "", chatViewModel: ChatViewModel = viewModel()) {
    chatViewModel.initChat(sentTo_Id)
    var message: String = chatViewModel.currentMessage
    val messages: List<Message> = chatViewModel.messageList

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "${chatViewModel.sentTo_User.data!!.firstName} ${chatViewModel.sentTo_User.data!!.lastName}",
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        }
        Divider(Modifier.fillMaxWidth())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(weight = 0.85f, fill = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = if(message.currentUser) Arrangement.End else Arrangement.Start) {
                    SingleMessage(message = message.message, isCurrentUser = message.currentUser)
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(bottom = 10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = {
                    chatViewModel.messageInputHandler(it)
                },
                label = {
                    Text(
                        "Type Your Message"
                    )
                },
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 1.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = false,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            chatViewModel.addMessage()
                            message = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Button"
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SingleMessage(message: String, isCurrentUser: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isCurrentUser) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
    ) {
        Text(
            text = message,
            textAlign =
            if (isCurrentUser)
                TextAlign.End
            else
                TextAlign.Start,
            modifier = Modifier
                .widthIn(max = 250.dp)
                .padding(16.dp),
            color = Color.White,
            style = MaterialTheme.typography.body2
        )
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    GuideMeTravelersAppTheme {
        ChatView()
    }
}
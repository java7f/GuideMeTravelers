package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemetravelersapp.dataModels.User
import com.example.guidemetravelersapp.dataModels.chat.ChatChannel
import com.example.guidemetravelersapp.dataModels.chat.Message
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.helpers.utils.Utils
import com.example.guidemetravelersapp.services.AuthenticationService
import com.example.guidemetravelersapp.services.ChatService
import com.example.guidemetravelersapp.services.LocationService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class ChatViewModel(application: Application): AndroidViewModel(application) {

    private val chatService: ChatService = ChatService()
    private val profileService: AuthenticationService = AuthenticationService(application)

    var currentMessage: String by mutableStateOf("")
    var currentChannelId: String by mutableStateOf("")
    var chatChannelsLoading: Boolean by mutableStateOf(false)
    var messageList: List<Message> by mutableStateOf(emptyList())
    var currentUserChannelList: HashMap<ChatChannel, User> by mutableStateOf(HashMap())

    var sentBy_User: ApiResponse<User> by mutableStateOf(ApiResponse(data = User(), inProgress = true))
    var sentTo_User: ApiResponse<User> by mutableStateOf(ApiResponse(data = User(), inProgress = true))

    init {
        initChatList()
    }

    fun initChatList() {
        getChannelsForCurrentUser()
    }

    fun initChat(sentTo_Id: String) {
        getUsersInChannel(sentTo_Id)
        chatService.getOrCreateChatChannel(sentTo_Id, this::updateChannelId)
    }

    /**
     * Handles the message input as the user types
     * @param message the message input
     */
    fun messageInputHandler(message: String) {
        currentMessage = message
    }

    /**
     * Sends the message
     */
    fun addMessage() {
        if(currentMessage.isNotEmpty()) {
            val newMessage: Message = Message(
                message = currentMessage,
                sent_by = sentBy_User.data!!.firebaseUserId,
                sent_to = sentTo_User.data!!.firebaseUserId,
            )
            chatService.sendMessage(newMessage, currentChannelId)
        }
    }

    fun updateMessages(messages: List<Message>) {
        messageList = messages.asReversed()
    }

    fun updateChannelId(channelId: String) {
        currentChannelId = channelId
        chatService.addChatMessagesListener(currentChannelId, this::updateMessages)
    }

    fun updateChannelList(channelList: List<ChatChannel>) {
        if (channelList.isNotEmpty()) {
            viewModelScope.launch {
                chatChannelsLoading = true
                val currentUserId = profileService.getCurrentFirebaseUserId()!!
                val channelsMap : MutableMap<ChatChannel, User> = HashMap()
                channelList.forEach { chatChannel ->
                    val toUserId: String = if(chatChannel.sentTo_Id == currentUserId) chatChannel.sentBy_Id else chatChannel.sentTo_Id

                    val toUser = profileService.getUserById(toUserId)
                    channelsMap.putIfAbsent(chatChannel, toUser!!)
                }
                currentUserChannelList = channelsMap as HashMap<ChatChannel, User>
                chatChannelsLoading = false
            }
        }
    }

    fun getUsersInChannel(sentTo_Id: String) {
        viewModelScope.launch {
            try {
                val sentBy = profileService.getCurrentFirebaseUserId()?.let {
                    profileService.getUserById(
                        it
                    )
                }
                val sentTo = profileService.getUserById(sentTo_Id)
                sentBy_User = ApiResponse(data = sentBy, inProgress = false)
                sentTo_User = ApiResponse(data = sentTo, inProgress = false)

            }
            catch (e: Exception) {
                Log.d(ChatViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                sentBy_User = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
                sentTo_User = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun getChannelsForCurrentUser() {
        chatService.getChannelsForCurrentUser(this::updateChannelList)
    }
}
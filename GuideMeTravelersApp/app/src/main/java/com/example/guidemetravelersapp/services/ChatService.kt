package com.example.guidemetravelersapp.services

import android.util.Log
import com.example.guidemetravelersapp.dataModels.chat.ChatChannel
import com.example.guidemetravelersapp.dataModels.chat.Message
import com.example.guidemetravelersapp.helpers.utils.Utils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase

class ChatService {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    /**
     * Gets a chat channel between 2 users or creates a new chat channel if it does not exists.
     * @param sentTo_Id Th Id of the user who receives a message
     * @param getChannelId callback function that manages the channelId
     */
    fun getOrCreateChatChannel(sentTo_Id: String, getChannelId: (channelId: String) -> Unit) {
        firestoreInstance.collection(Utils.CHAT_CHANNELS).get().addOnSuccessListener { channels ->
            val currentUserId = Firebase.auth.currentUser?.uid
            for (channel in channels) {
                if( //Check if there is an existing channel between the 2 specified users
                    (channel[Utils.SENT_BY_ID]!! == currentUserId || channel[Utils.SENT_TO_ID]!! == currentUserId)
                    &&
                    (channel[Utils.SENT_BY_ID]!! == sentTo_Id || channel[Utils.SENT_TO_ID]!! == sentTo_Id)
                ) {
                    getChannelId(channel.id)
                    return@addOnSuccessListener
                }
            }

            //If no channel exists, create a new one
            val newChannel = firestoreInstance.collection(Utils.CHAT_CHANNELS).document()
            newChannel.set(
                ChatChannel(
                    sentBy_Id = currentUserId!!,
                    sentTo_Id = sentTo_Id
                )
            )

            getChannelId(newChannel.id)
        }
    }

    /**
     * Sends a message by saving it in the chat channel document
     * @param message The message to be sent
     * @param channelId the channel where the message belongs to
     */
    fun sendMessage(message: Message, channelId: String) {
        firestoreInstance.collection(Utils.CHAT_CHANNELS).document(channelId)
            .collection("messages")
            .add(message)
    }

    /**
     * Listener that will listen to new messages inside a channel
     * @param channelId the channel to listen to new messages
     * @param onListen callback function that will receive the list of messages
     */
    fun addChatMessagesListener(channelId: String, onListen: (List<Message>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection(Utils.CHAT_CHANNELS).document(channelId).collection(Utils.MESSAGES)
            .orderBy(Utils.SENT_ON)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val messages = mutableListOf<Message>()
                querySnapshot!!.documents.forEach {
                    val isCurrentUser = Firebase.auth.currentUser?.uid.toString() == it.data!![Utils.SENT_BY].toString()
                    val message = it.toObject(Message::class.java)!!
                    message.currentUser = isCurrentUser
                    messages.add(message)
                    return@forEach
                }
                onListen(messages)
            }
    }

    fun getChannelsForCurrentUser(getChannels: (channels: List<ChatChannel>) -> Unit) {
        val currentUserId: String? = Firebase.auth.currentUser?.uid

        firestoreInstance.collection(Utils.CHAT_CHANNELS).get().addOnSuccessListener { channels ->
            var channelsForUser: MutableList<ChatChannel> = mutableListOf()
            for (channel in channels) {
                if( //Check for channels where the current user is involved
                    (channel[Utils.SENT_BY_ID]!! == currentUserId || channel[Utils.SENT_TO_ID]!! == currentUserId)
                ) {
                    channelsForUser.add(channel.toObject(ChatChannel::class.java))
                }
            }
            getChannels(channelsForUser)
            return@addOnSuccessListener
        }
    }

    /**
     * Removes the given listener instance
     */
    fun removeListener(listener: ListenerRegistration) = listener.remove()
}
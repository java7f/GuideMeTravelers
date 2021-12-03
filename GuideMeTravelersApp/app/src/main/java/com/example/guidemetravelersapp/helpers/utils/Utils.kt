package com.example.guidemetravelersapp.helpers.utils

object Utils {

    //---------------------- CHAT CONSTANTS ----------------------//
    const val MESSAGES = "messages"
    const val MESSAGE = "message"
    const val SENT_BY = "sent_by"
    const val SENT_ON = "sent_on"
    const val SENT_BY_ID = "sentBy_Id"
    const val SENT_TO_ID = "sentTo_Id"
    const val IS_CURRENT_USER = "currentUser"

    //---------------------- FIRESTORE COLLECTIONS ----------------------//
    const val CHAT_CHANNELS = "chat_channels"

    //---------------------- ENCRYPT/DECRYPT ----------------------//
    const val CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"
    const val KEY_SPEC_ALGORITHM = "AES"
    const val KEYSTORE_NAME = "AndroidKeyStore"
    const val ENCRYPTION_KEY_NAME = "aes_key"
    const val TEMP_FILE_NAME = "temp"
    const val TEMP_FILE_EXT = ".mp3"
    const val DIR_NAME = "Audio"
    const val LOCATIONPHOTO_DIR_NAME = "Locations"

    //---------------------- SHAREPREFS KEYS ----------------------//
    const val OFFLINE_MODE = "offline_mode"

    //---------------------- ROOM TABLE/ENTITY NAMES ----------------------//
    const val LOCATIONS_TABLE = "locations"
    const val AUDIOGUIDES_TABLE = "audioguides"
    const val LOCAL_URI = "local_uri"

    //---------------------- NOTIFICATION CHANNEL/MANAGER ----------------------//
    const val CHANNEL_ID = "com.example.guidemetravelersapp.channel1"
    const val CHANNEL_NAME = "ProximityAudioguide"
    const val CHANNEL_DESCRIPTION = "For recommended proximity audioguides"
    const val NOTIFICATION_ID = 77
    const val NOTIFICATION_TITLE = "Audioguides were detected near you!"

    //---------------------- NOTIFICATION KEYS ----------------------//
    const val TITLE_NOTIFICATION_KEY = "title"
    const val BODY_NOTIFICATION_KEY = "body"
}
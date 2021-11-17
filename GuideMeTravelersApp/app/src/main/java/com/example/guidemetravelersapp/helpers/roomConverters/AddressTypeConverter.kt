package com.example.guidemetravelersapp.helpers.roomConverters

import androidx.room.TypeConverter
import com.example.guidemetravelersapp.dataModels.Address
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class AddressTypeConverter {

    @TypeConverter
    fun addressToStoredString(address: Address): String {
        val gson = Gson()
        val type = object : TypeToken<Address>() {}.type
        return gson.toJson(address, type)
    }

    @TypeConverter
    fun storedStringToAddress(value: String): Address {
        val gson = Gson()
        val type = object : TypeToken<Address>() {}.type
        return gson.fromJson(value, type)
    }
}
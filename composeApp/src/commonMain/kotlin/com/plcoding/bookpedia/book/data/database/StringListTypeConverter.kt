package com.plcoding.bookpedia.book.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object StringListTypeConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.decodeFromString<List<String>>(value)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(list)
    }
}
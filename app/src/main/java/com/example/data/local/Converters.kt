package com.example.data.local

import androidx.room.TypeConverter
import org.json.JSONArray
import org.json.JSONObject

/**
 * Room TypeConverters for serializing lists and maps to/from SQLite columns.
 */
class Converters {

    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            val jsonArray = JSONArray(value)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String {
        if (list.isNullOrEmpty()) return "[]"
        val jsonArray = JSONArray()
        list.forEach { jsonArray.put(it) }
        return jsonArray.toString()
    }

    @TypeConverter
    fun fromStringMap(value: String?): Map<String, String> {
        if (value.isNullOrBlank()) return emptyMap()
        return try {
            val jsonObject = JSONObject(value)
            val map = mutableMapOf<String, String>()
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                map[key] = jsonObject.getString(key)
            }
            map
        } catch (_: Exception) {
            emptyMap()
        }
    }

    @TypeConverter
    fun toStringMap(map: Map<String, String>?): String {
        if (map.isNullOrEmpty()) return "{}"
        val jsonObject = JSONObject()
        map.forEach { (k, v) -> jsonObject.put(k, v) }
        return jsonObject.toString()
    }
}

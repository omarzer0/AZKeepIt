package az.zero.azkeepit.data.local.helper

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// TODO(improve) Don't create Gson object each time.
@ProvidedTypeConverter
class CustomTypeConverters {

    @TypeConverter
    fun listOfStringToString(list: List<String>?): String? {
        return list?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun stringToListOfString(string: String?): List<String>? {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return string?.let { Gson().fromJson(it, listType) }
    }

}
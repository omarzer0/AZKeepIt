package az.zero.azkeepit.ui.screens

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

inline fun <reified T : Any> serializableNavType(
    serializer: KSerializer<T> = serializer(),
    isNullableAllowed: Boolean = false,
    json: Json = Json
): NavType<T> {
    return object : NavType<T>(isNullableAllowed) {
        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putString(key, json.encodeToString(serializer, value))
        }

        override fun get(bundle: Bundle, key: String): T {
            return json.decodeFromString(serializer, bundle.getString(key)!!)
        }

        override fun parseValue(value: String): T {
            return json.decodeFromString(serializer, value)
        }

        override fun serializeAsValue(value: T): String {
            return json.encodeToString(serializer, value)
        }
    }
}
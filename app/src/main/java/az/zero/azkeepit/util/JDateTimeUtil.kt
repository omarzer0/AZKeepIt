package az.zero.azkeepit.util

import android.util.Log
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

object JDateTimeUtil {

    private const val TAG = "JDateTimeUtil"

    fun now(): Long {
        return ZonedDateTime.now().toEpochSecond()
    }

    fun toShortDateTimeFormat(dateTime: Long): String = try {
        val zonedDateTime = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(dateTime),
            ZoneId.systemDefault()
        )

        // For some reason LLL format isn't working
        // return zonedDateTime.format(DateTimeFormatter.ofPattern("LLL dd", Locale.ENGLISH))

        val monthName = zonedDateTime.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        val dayNumber = zonedDateTime.format(DateTimeFormatter.ofPattern("dd"))
        "$monthName $dayNumber"
    } catch (e: Exception) {
        Log.e(TAG, "toLongDateTimeFormat: ${e.localizedMessage}")
        ""
    }

    fun toLongDateTimeFormat(dateTime: Long): String = try {
        val zonedDateTime = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(dateTime),
            ZoneId.systemDefault()
        )
        // For some reason LLL format isn't working
        // return zonedDateTime.format(DateTimeFormatter.ofPattern("dd yyyy, HH:mm E", Locale.ENGLISH))

        val formatted =
            zonedDateTime.format(DateTimeFormatter.ofPattern("dd yyyy, HH:mm E", Locale.ENGLISH))
        val monthName = zonedDateTime.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        "$monthName $formatted"
    } catch (e: Exception) {
        Log.e(TAG, "toLongDateTimeFormat: ${e.localizedMessage}")
        ""
    }

}


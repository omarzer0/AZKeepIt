package az.zero.azkeepit.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun main() {

    val now = ZonedDateTime.now().toEpochSecond()
    println("now $now")


    val dateTime = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(now),
        ZoneId.systemDefault()
    )
    println("date $dateTime")

    val shortPattern = dateTime.format(DateTimeFormatter.ofPattern("LLL dd"))
    println(shortPattern)


    val longPattern = dateTime.format(DateTimeFormatter.ofPattern("LLL dd yyyy, HH:mm E"))
    println(longPattern)

}
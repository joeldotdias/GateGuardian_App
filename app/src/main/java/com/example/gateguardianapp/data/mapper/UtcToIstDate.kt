package com.example.gateguardianapp.data.mapper

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun getIstDateTime(dateStr: String): String {
    val dateFmt = "yyyy-MM-dd HH:mm:ss"
    val dateTimeZone = TimeZone.getTimeZone("UTC")
    val parser = SimpleDateFormat(dateFmt, Locale.getDefault())
    parser.timeZone = dateTimeZone

    val parsedDate = parser.parse(dateStr.replace('T', ' ').replace("Z", ""))

    val currTimeZone = TimeZone.getDefault()
    val targetFmt = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    targetFmt.timeZone = currTimeZone

    return parsedDate?.let {
        targetFmt.format(it)
    } ?: ""
}
package com.android.todoagain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getTodayDate(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMM")
    return current.format(formatter)
}

fun formatDate(day: Int, month: Int, year:Int): String{
    return "$day / $month / $year"
}

fun main() {
    print(getTodayDate())
}
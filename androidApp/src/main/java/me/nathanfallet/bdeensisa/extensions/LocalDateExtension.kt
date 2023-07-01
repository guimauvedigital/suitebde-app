package me.nathanfallet.bdeensisa.extensions

import kotlinx.datetime.LocalDate
import java.util.Calendar

val oneYear: LocalDate
    get() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, Calendar.JULY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.YEAR, 1)
        }
        return LocalDate(calendar.get(Calendar.YEAR), 7, 31)
    }

val fiveYears: LocalDate
    get() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, Calendar.JULY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.YEAR, 5)
        } else {
            calendar.add(Calendar.YEAR, 4)
        }
        return LocalDate(calendar.get(Calendar.YEAR), 7, 31)
    }

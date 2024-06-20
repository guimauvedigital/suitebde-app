package com.suitebde.extensions

import kotlinx.datetime.LocalDate
import java.util.*

val oneDay: LocalDate
    get() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return LocalDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

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

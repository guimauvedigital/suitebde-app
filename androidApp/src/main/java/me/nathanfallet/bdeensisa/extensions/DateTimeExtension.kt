package me.nathanfallet.bdeensisa.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaLocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val LocalDate.formatted: String
    get() = DateTimeFormatter
        .ofPattern("dd/MM/yyyy")
        .format(toJavaLocalDate())

val Instant.formatted: String
    get() = DateTimeFormatter
        .ofPattern("dd/MM/yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
        .format(toJavaInstant())

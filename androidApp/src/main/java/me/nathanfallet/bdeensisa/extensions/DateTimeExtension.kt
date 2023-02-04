package me.nathanfallet.bdeensisa.extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

val LocalDate.formatted: String
    get() = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(
        toJavaLocalDate()
    )

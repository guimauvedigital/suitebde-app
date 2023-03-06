package me.nathanfallet.bdeensisa.extensions

import kotlinx.datetime.*

val Instant.renderedDateTime: String
    get() {
        return toLocalDateTime(TimeZone.currentSystemDefault()).renderedDateTime
    }

val LocalDateTime.renderedDateTime: String
    get() {
        return "$renderedDate à $renderedTime"
    }

val LocalDateTime.renderedDate: String
    get() {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        if (dayOfYear == now.dayOfYear && year == now.year) {
            return "Aujourd'hui"
        }
        if (dayOfYear == now.dayOfYear + 1 && year == now.year) {
            return "Demain"
        }
        if (monthNumber == now.monthNumber && year == now.year) {
            return "${dayOfWeek.frenchName} $dayOfMonth"
        }
        if (year == now.year) {
            return "$dayOfMonth ${month.frenchName}"
        }
        return "$dayOfMonth ${month.frenchName} $year"
    }

val LocalDateTime.renderedTime: String
    get() {
        return "$hour:${minute.toString().padStart(2, '0')}"
    }

val LocalDate.renderedDate: String
    get() {
        return "${toString()}T00:00:00Z".toInstant()
            .toLocalDateTime(TimeZone.currentSystemDefault()).renderedDate
    }

val DayOfWeek.frenchName: String
    get() {
        return when (this) {
            DayOfWeek.MONDAY -> "Lundi"
            DayOfWeek.TUESDAY -> "Mardi"
            DayOfWeek.WEDNESDAY -> "Mercredi"
            DayOfWeek.THURSDAY -> "Jeudi"
            DayOfWeek.FRIDAY -> "Vendredi"
            DayOfWeek.SATURDAY -> "Samedi"
            DayOfWeek.SUNDAY -> "Dimanche"
            else -> "Inconnu"
        }
    }

val Month.frenchName: String
    get() {
        return when (this) {
            Month.JANUARY -> "Janvier"
            Month.FEBRUARY -> "Février"
            Month.MARCH -> "Mars"
            Month.APRIL -> "Avril"
            Month.MAY -> "Mai"
            Month.JUNE -> "Juin"
            Month.JULY -> "Juillet"
            Month.AUGUST -> "Août"
            Month.SEPTEMBER -> "Septembre"
            Month.OCTOBER -> "Octobre"
            Month.NOVEMBER -> "Novembre"
            Month.DECEMBER -> "Décembre"
            else -> "Inconnu"
        }
    }

package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ScanHistoryEntry(
    val userId: String,
    val scannerId: String,
    val scannedAt: Instant,
    val type: String,
    val user: User? = null,
    val scanner: User? = null,
    val event: Event? = null
)


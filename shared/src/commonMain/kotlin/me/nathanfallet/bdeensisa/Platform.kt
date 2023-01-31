package me.nathanfallet.bdeensisa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
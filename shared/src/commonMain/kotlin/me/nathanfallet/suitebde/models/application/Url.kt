package me.nathanfallet.suitebde.models.application

data class Url(
    val scheme: String?,
    val host: String?,
    val path: String?,
) {

    val pathSegments: List<String>?
        get() = path?.split("/")?.filter { it.isNotEmpty() }

}

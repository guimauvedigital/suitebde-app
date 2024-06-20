package com.suitebde.models.ensisa

import dev.kaccelero.serializers.Serialization
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
data class User(
    val id: String,
    val email: String?,
    val password: String?,
    val firstName: String?,
    val lastName: String?,
    val option: String?,
    val year: String?,
    val cotisant: Cotisant? = null,
    var permissions: List<String>? = null,
) {

    // Json API

    companion object {

        fun fromJson(json: String): User = Serialization.json.decodeFromString(json)
        fun toJson(user: User): String = Serialization.json.encodeToString(user)

    }

    // User API

    fun hasPermission(permission: String): Boolean {
        // Construct allowed permissions
        // i.e. for "admin.users.view" we will check "admin.users.view", "admin.users.*" and "admin.*"
        val allowedPermissions = mutableListOf(permission)
        while (allowedPermissions.last().replace(".*", "").contains('.')) {
            allowedPermissions.add(allowedPermissions.last().replace(".*", "").substringBeforeLast('.') + ".*")
        }
        return permissions?.any { allowedPermissions.contains(it) } ?: false
    }

    val description: String
        get() {
            val optionStr = when (option) {
                "ir" -> "Informatique et Réseaux"
                "ase" -> "Automatique et Systèmes embarqués"
                "meca" -> "Mécanique"
                "tf" -> "Textile et Fibres"
                "gi" -> "Génie Industriel"
                else -> "Inconnu"
            }
            return "$year - $optionStr"
        }

    val hasPermissions: Boolean
        get() {
            return (permissions ?: listOf()).isNotEmpty()
        }

}

@Serializable
data class UserAuthorize(
    val code: String,
)

@Serializable
data class UserToken(
    val token: String,
    val user: User,
)

@Serializable
data class UserUpload(
    val firstName: String? = null,
    val lastName: String? = null,
    val year: String? = null,
    val option: String? = null,
    val expiration: String? = null,
)


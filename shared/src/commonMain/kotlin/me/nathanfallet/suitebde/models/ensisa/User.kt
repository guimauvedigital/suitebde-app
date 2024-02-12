package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.usecases.auth.AuthToken

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

        fun fromJson(json: String): User {
            return Json.decodeFromString(json)
        }

        fun toJson(user: User): String {
            return Json.encodeToString(user)
        }

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

    val suiteBde = me.nathanfallet.suitebde.models.users.User(
        id,
        "",
        email ?: "",
        password ?: "",
        firstName ?: "",
        lastName ?: "",
        false
    )

}

@Serializable
data class UserAuthorize(
    val code: String,
)

@Serializable
data class UserToken(
    val token: String,
    val user: User,
) {

    val suiteBde = AuthToken(
        token, token,
        "bdensisa/${user.id}"
    )

}

@Serializable
data class UserUpload(
    val firstName: String? = null,
    val lastName: String? = null,
    val year: String? = null,
    val option: String? = null,
    val expiration: String? = null,
) {

    constructor(payload: UpdateUserPayload) : this(
        payload.firstName,
        payload.lastName,
    )

}


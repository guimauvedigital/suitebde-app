package me.nathanfallet.suitebde.models.ensisa

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Club(
    val id: String,
    val name: String,
    val description: String?,
    val information: String?,
    val createdAt: Instant?,
    val validated: Boolean?,
    val email: String?,
    val logo: String?,
    val membersCount: Long?,
) {

    val suiteBde = me.nathanfallet.suitebde.models.clubs.Club(
        id,
        "",
        name,
        description ?: "",
        logo?.let { "https://bdensisa.org/clubs/$id/uploads/$it" },
        createdAt ?: Instant.DISTANT_PAST,
        validated ?: false,
        membersCount ?: 0,
        false,
    )

}

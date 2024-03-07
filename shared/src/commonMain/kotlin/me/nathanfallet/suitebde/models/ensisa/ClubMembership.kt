package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.clubs.RoleInClub

@Serializable
data class ClubMembership(
    val userId: String,
    val clubId: String,
    val role: String,
    val user: User? = null,
    val club: Club? = null,
) {

    val suiteBde = me.nathanfallet.suitebde.models.clubs.UserInClub(
        userId,
        clubId,
        role,
        user?.suiteBde,
        club?.suiteBde(false),
        RoleInClub(
            role,
            clubId,
            role,
            false,
        )
    )

}

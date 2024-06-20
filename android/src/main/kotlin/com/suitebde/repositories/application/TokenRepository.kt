package com.suitebde.repositories.application

import android.content.Context
import dev.kaccelero.models.UUID

class TokenRepository(
    context: Context,
) : ITokenRepository {

    private val sharedPreferences = context.getSharedPreferences("suitebde", Context.MODE_PRIVATE)

    override fun getToken(): String? = sharedPreferences.getString("token", null)

    override fun setToken(token: String?) = sharedPreferences.edit()
        .putString("token", token)
        .apply()

    override fun getRefreshToken(): String? = sharedPreferences.getString("refreshToken", null)

    override fun setRefreshToken(token: String?) = sharedPreferences.edit()
        .putString("refreshToken", token)
        .apply()

    override fun getUserId(): UUID? = sharedPreferences.getString("userId", null)?.let(::UUID)

    override fun setUserId(userId: UUID?) = sharedPreferences.edit()
        .putString("userId", userId.toString())
        .apply()

    override fun getAssociationId(): UUID? = sharedPreferences.getString("associationId", null)?.let(::UUID)

    override fun setAssociationId(associationId: UUID?) = sharedPreferences.edit()
        .putString("associationId", associationId.toString())
        .apply()

    override fun getFcmToken(): String? = sharedPreferences.getString("fcmToken", null)

    override fun setFcmToken(fcmToken: String?) = sharedPreferences.edit()
        .putString("fcmToken", fcmToken)
        .apply()

}

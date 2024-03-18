package me.nathanfallet.suitebde.repositories.application

import android.content.Context

class TokenRepository(
    context: Context,
) : ITokenRepository {

    private val sharedPreferences = context.getSharedPreferences("suitebde", Context.MODE_PRIVATE)

    override fun getToken(): String? = sharedPreferences.getString("token", null)

    override fun setToken(token: String?) = sharedPreferences.edit()
        .putString("token", token)
        .apply()

    override fun getUserId(): String? = sharedPreferences.getString("userId", null)

    override fun setUserId(userId: String?) = sharedPreferences.edit()
        .putString("userId", userId)
        .apply()

    override fun getAssociationId(): String? = sharedPreferences.getString("associationId", null)

    override fun setAssociationId(associationId: String?) = sharedPreferences.edit()
        .putString("associationId", associationId)
        .apply()

    override fun getFcmToken(): String? = sharedPreferences.getString("fcmToken", null)

    override fun setFcmToken(fcmToken: String?) = sharedPreferences.edit()
        .putString("fcmToken", fcmToken)
        .apply()

}

package me.nathanfallet.suitebde.repositories.application

import android.content.Context

class TokenRepository(
    context: Context,
) : ITokenRepository {

    private val sharedPreferences = context.getSharedPreferences("suitebde", Context.MODE_PRIVATE)

    override fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    override fun setToken(token: String?) {
        sharedPreferences.edit()
            .putString("token", token)
            .apply()
    }

    override fun getUserId(): String? {
        return sharedPreferences.getString("userId", null)
    }

    override fun setUserId(userId: String?) {
        sharedPreferences.edit()
            .putString("userId", userId)
            .apply()
    }

    override fun getAssociationId(): String? {
        return sharedPreferences.getString("associationId", null)
    }

    override fun setAssociationId(associationId: String?) {
        sharedPreferences.edit()
            .putString("associationId", associationId)
            .apply()
    }

}

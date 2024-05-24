package me.nathanfallet.suitebde.repositories.application

interface ITokenRepository {

    fun getToken(): String?
    fun setToken(token: String?)
    fun getRefreshToken(): String?
    fun setRefreshToken(token: String?)

    fun getUserId(): String?
    fun setUserId(userId: String?)

    fun getAssociationId(): String?
    fun setAssociationId(associationId: String?)

    fun getFcmToken(): String?
    fun setFcmToken(fcmToken: String?)

}

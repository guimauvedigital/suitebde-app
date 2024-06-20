package com.suitebde.repositories.application

import dev.kaccelero.models.UUID

interface ITokenRepository {

    fun getToken(): String?
    fun setToken(token: String?)
    fun getRefreshToken(): String?
    fun setRefreshToken(token: String?)

    fun getUserId(): UUID?
    fun setUserId(userId: UUID?)

    fun getAssociationId(): UUID?
    fun setAssociationId(associationId: UUID?)

    fun getFcmToken(): String?
    fun setFcmToken(fcmToken: String?)

}

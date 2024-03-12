package me.nathanfallet.suitebde.services

import me.nathanfallet.ktorx.repositories.auth.IAuthAPIRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.application.SearchOptions
import me.nathanfallet.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.clubs.*
import me.nathanfallet.suitebde.models.ensisa.EventUpload
import me.nathanfallet.suitebde.models.ensisa.UserUpload
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.stripe.CheckoutSession
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.repositories.associations.ISubscriptionsInAssociationsRemoteRepository
import me.nathanfallet.suitebde.repositories.clubs.IClubsRemoteRepository
import me.nathanfallet.suitebde.repositories.clubs.IUsersInClubsRemoteRepository
import me.nathanfallet.suitebde.repositories.events.IEventsRemoteRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRemoteRepository
import me.nathanfallet.usecases.auth.AuthRequest
import me.nathanfallet.usecases.pagination.Pagination

class EnsisaClient(
    private val client: ISuiteBDEClient,
) : ISuiteBDEClient by client {

    private val apiService = APIService()

    // That will be updated dynamically when the new backend will be deployed
    // (so that users can still use the app without updating it, to avoid breaking changes)
    private var shouldUseMapping = true

    private val token: String?
        get() = client.getTokenUseCase?.invoke()

    private val authMapping = object : IAuthAPIRemoteRepository {

        override suspend fun token(payload: AuthRequest) =
            apiService.authenticate(payload.code).suiteBde

    }

    private val usersMapping = object : IUsersRemoteRepository {

        override suspend fun get(id: String, associationId: String) =
            apiService.getUser(token!!, id).suiteBde

        override suspend fun list(pagination: Pagination, associationId: String) =
            apiService.getUsers(
                token!!, pagination.offset, pagination.limit,
                (pagination.options as? SearchOptions)?.search
            ).map { it.suiteBde }

        override suspend fun update(id: String, payload: UpdateUserPayload, associationId: String) =
            apiService.updateUser(token!!, id, UserUpload(payload)).suiteBde

        override suspend fun listPermissions(id: String, associationId: String): List<Permission> =
            emptyList() // TODO: Convert from old permissions to new ones

    }

    private val subscriptionsInAssociationsMapping = object : ISubscriptionsInAssociationsRemoteRepository {

        override suspend fun list(pagination: Pagination, associationId: String): List<SubscriptionInAssociation> =
            apiService.getCotisantConfigurations().map { it.suiteBde }

        override suspend fun get(id: String, associationId: String): SubscriptionInAssociation? =
            apiService.getCotisantConfigurations().firstOrNull { it.id == id }?.suiteBde

        override suspend fun create(
            payload: CreateSubscriptionInAssociationPayload,
            associationId: String,
        ): SubscriptionInAssociation? = null

        override suspend fun delete(id: String, associationId: String): Boolean = false

        override suspend fun update(
            id: String,
            payload: UpdateSubscriptionInAssociationPayload,
            associationId: String,
        ): SubscriptionInAssociation? = null

        override suspend fun checkout(id: String, associationId: String): CheckoutSession =
            apiService.createShopItem(token!!, "cotisants", id).suiteBde

    }

    private val eventsMapping = object : IEventsRemoteRepository {

        override suspend fun create(payload: CreateEventPayload, associationId: String) =
            apiService.suggestEvent(token!!, EventUpload(payload)).suiteBde

        override suspend fun delete(id: String, associationId: String) =
            false // Not supported by the old API

        override suspend fun get(id: String, associationId: String) =
            apiService.getEvent(id).suiteBde

        override suspend fun list(pagination: Pagination, associationId: String) =
            apiService.getEvents(pagination.offset, pagination.limit).map { it.suiteBde }

        override suspend fun update(id: String, payload: UpdateEventPayload, associationId: String) =
            apiService.updateEvent(token!!, id, EventUpload(payload)).suiteBde

    }

    private val clubsMapping = object : IClubsRemoteRepository {

        override suspend fun create(payload: CreateClubPayload, associationId: String) =
            null // Not supported by the old API

        override suspend fun delete(id: String, associationId: String) =
            false // Not supported by the old API

        override suspend fun get(id: String, associationId: String): Club {
            val myClubs = apiService.getClubsMe(token!!)
            return apiService.getClub(id).suiteBde(myClubs.any { it.clubId == id })
        }

        override suspend fun list(pagination: Pagination, associationId: String): List<Club> {
            val myClubs =
                if (pagination.offset == 0L && pagination.options !is SearchOptions) apiService.getClubsMe(token!!)
                    .map { it.club!!.suiteBde(true) }
                else emptyList()
            val clubs =
                if (pagination.options is SearchOptions) emptyList()
                else apiService.getClubs(pagination.offset, pagination.limit).map { it.suiteBde(false) }
            return clubs.filter { myClubs.none { c -> it.id == c.id } } + myClubs
        }

        override suspend fun update(id: String, payload: UpdateClubPayload, associationId: String) =
            null // Not supported by the old API

    }

    private val usersInClubsMapping = object : IUsersInClubsRemoteRepository {

        override suspend fun create(
            payload: CreateUserInClubPayload,
            clubId: String,
            associationId: String,
        ): UserInClub = apiService.joinClub(token!!, clubId).suiteBde

        override suspend fun delete(userId: String, clubId: String, associationId: String): Boolean =
            apiService.leaveClub(token!!, clubId).let { true }

        override suspend fun list(pagination: Pagination, clubId: String, associationId: String): List<UserInClub> =
            if (pagination.offset == 0L) apiService.getClubMembers(clubId).map { it.suiteBde } else emptyList()

    }

    override val auth: IAuthAPIRemoteRepository
        get() = if (shouldUseMapping) authMapping else client.auth

    override val subscriptionsInAssociations: ISubscriptionsInAssociationsRemoteRepository
        get() = if (shouldUseMapping) subscriptionsInAssociationsMapping else client.subscriptionsInAssociations

    override val users: IUsersRemoteRepository
        get() = if (shouldUseMapping) usersMapping else client.users

    override val events: IEventsRemoteRepository
        get() = if (shouldUseMapping) eventsMapping else client.events

    override val clubs: IClubsRemoteRepository
        get() = if (shouldUseMapping) clubsMapping else client.clubs

    override val usersInClubs: IUsersInClubsRemoteRepository
        get() = if (shouldUseMapping) usersInClubsMapping else client.usersInClubs

}

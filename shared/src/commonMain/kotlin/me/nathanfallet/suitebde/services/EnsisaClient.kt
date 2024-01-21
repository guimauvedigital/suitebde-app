package me.nathanfallet.suitebde.services

import me.nathanfallet.ktorx.repositories.auth.IAuthAPIRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.suitebde.models.ensisa.EventUpload
import me.nathanfallet.suitebde.models.ensisa.UserUpload
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.repositories.clubs.IClubsRemoteRepository
import me.nathanfallet.suitebde.repositories.events.IEventsRemoteRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRemoteRepository
import me.nathanfallet.usecases.auth.AuthRequest

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

        override suspend fun list(limit: Long, offset: Long, associationId: String) =
            apiService.getUsers(token!!, offset, limit).map { it.suiteBde }

        override suspend fun update(id: String, payload: UpdateUserPayload, associationId: String) =
            apiService.updateUser(token!!, id, UserUpload(payload)).suiteBde

    }

    private val eventsMapping = object : IEventsRemoteRepository {

        override suspend fun create(payload: CreateEventPayload, associationId: String) =
            apiService.suggestEvent(token!!, EventUpload(payload)).suiteBde

        override suspend fun delete(id: String, associationId: String) =
            false // Not supported by the old API

        override suspend fun get(id: String, associationId: String) =
            apiService.getEvent(id).suiteBde

        override suspend fun list(limit: Long, offset: Long, associationId: String) =
            apiService.getEvents(offset, limit).map { it.suiteBde }

        override suspend fun update(id: String, payload: UpdateEventPayload, associationId: String) =
            apiService.updateEvent(token!!, id, EventUpload(payload)).suiteBde

    }

    private val clubsMapping = object : IClubsRemoteRepository {

        override suspend fun create(payload: CreateClubPayload, associationId: String) =
            null // Not supported by the old API

        override suspend fun delete(id: String, associationId: String) =
            false // Not supported by the old API

        override suspend fun get(id: String, associationId: String) =
            apiService.getClub(id).suiteBde

        override suspend fun list(limit: Long, offset: Long, associationId: String) =
            apiService.getClubs(offset, limit).map { it.suiteBde }

        override suspend fun update(id: String, payload: UpdateClubPayload, associationId: String) =
            null // Not supported by the old API

    }

    override val auth: IAuthAPIRemoteRepository
        get() = if (shouldUseMapping) authMapping else client.auth

    override val users: IUsersRemoteRepository
        get() = if (shouldUseMapping) usersMapping else client.users

    override val events: IEventsRemoteRepository
        get() = if (shouldUseMapping) eventsMapping else client.events

    override val clubs: IClubsRemoteRepository
        get() = if (shouldUseMapping) clubsMapping else client.clubs

}

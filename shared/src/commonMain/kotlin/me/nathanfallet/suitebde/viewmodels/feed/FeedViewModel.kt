package me.nathanfallet.suitebde.viewmodels.feed

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.usecases.events.IFetchEventsUseCase

class FeedViewModel(
    private val fetchEventsUseCase: IFetchEventsUseCase,
) : KMMViewModel() {

    // Properties

    private val _events = MutableStateFlow<List<Event>?>(viewModelScope, null)

    @NativeCoroutinesState
    val events = _events.asStateFlow()

    // Methods

    @NativeCoroutines
    suspend fun fetchFeed() {
        fetchEvents()
    }

    @NativeCoroutines
    suspend fun fetchEvents() {
        _events.value = fetchEventsUseCase(5, 0)
    }

}

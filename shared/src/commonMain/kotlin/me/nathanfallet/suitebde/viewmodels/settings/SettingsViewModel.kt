package me.nathanfallet.suitebde.viewmodels.settings

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

class SettingsViewModel(
    private val logEventUseCase: ILogEventUseCase,
) : KMMViewModel() {

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("settings"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("SettingsView")
            )
        )
    }

}

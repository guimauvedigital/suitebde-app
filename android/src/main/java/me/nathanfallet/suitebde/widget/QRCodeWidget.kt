package me.nathanfallet.suitebde.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.generateQRCode
import me.nathanfallet.suitebde.models.ensisa.User
import me.nathanfallet.suitebde.services.StorageService

class QRCodeWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.qr_code_widget)

    // Get data
    val prefs = StorageService.getInstance(context).sharedPreferences
    prefs.getString("user", null)?.let {
        val user = User.fromJson(it)
        views.setImageViewBitmap(
            R.id.widget_image,
            "bdeensisa://users/${user.id}".generateQRCode()
        )
    } ?: run {
        views.setTextViewText(
            R.id.widget_text,
            "Veuillez vous connectez à votre compte pour obtenir votre QR Code."
        )
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

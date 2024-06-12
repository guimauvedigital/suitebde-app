package me.nathanfallet.suitebde.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.generateQRCode
import me.nathanfallet.suitebde.repositories.application.TokenRepository

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
    val views = RemoteViews(context.packageName, R.layout.qr_code_widget)

    val repository = TokenRepository(context)
    val associationId = repository.getAssociationId()
    val userId = repository.getUserId()

    if (associationId != null && userId != null) views.setImageViewBitmap(
        R.id.widget_image,
        "suitebde://users/$associationId/$userId".generateQRCode()
    ) else views.setTextViewText(
        R.id.widget_text,
        "Veuillez vous connectez à votre compte pour obtenir votre QR Code."
    )

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

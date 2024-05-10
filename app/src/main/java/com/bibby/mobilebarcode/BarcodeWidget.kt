package com.bibby.mobilebarcode

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.bibby.mobilebarcode.MainActivity.Companion.ONCLICKBARCODE
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder


/**
 * Implementation of App Widget functionality.
 */
class BarcodeWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
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

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (ONCLICKBARCODE == intent?.action){
            val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // When setting the clipboard text.
            clipboardManager.setPrimaryClip(ClipData.newPlainText   ("", context.getSharedPreferences("bibby", Context.MODE_PRIVATE)
                ?.getString("mobilebarcode", "")))
            // Only show a toast for Android 12 and lower.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    Log.d("bibby", "updateAppWidget")

    // val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.barcode_widget)

    val multiFormatWriter = MultiFormatWriter()

    val bitMatrix = multiFormatWriter.encode(
        context.getSharedPreferences("bibby", Context.MODE_PRIVATE).getString("mobilebarcode", ""),
        BarcodeFormat.CODE_39, 900, 300)

    val barcodeEncoder = BarcodeEncoder()
    val bitmap = barcodeEncoder.createBitmap(bitMatrix)
    views.setImageViewBitmap(R.id.appwidget_image, bitmap)

    val intent = Intent(context, BarcodeWidget::class.java)
    intent.action = ONCLICKBARCODE
    val pending = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    views.setOnClickPendingIntent(R.id.appwidget_image, pending)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
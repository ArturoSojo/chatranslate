package com.example.chatranslate

import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Looper
import android.provider.MediaStore
import java.util.logging.Handler

class WhatsAppMediaObserver(
    private val context: Context,
    private val onNewImageDetected: (Uri) -> Unit
) : ContentObserver(android.os.Handler(Looper.getMainLooper())) {

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)

        uri ?: return

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )

        context.contentResolver.query(
            uri, projection, null, null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val fileUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                )
                onNewImageDetected(fileUri)
            }
        }
    }
}

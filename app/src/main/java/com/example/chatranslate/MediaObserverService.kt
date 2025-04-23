package com.example.chatranslate

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import android.util.Log

class MediaObserverService : Service() {

    private lateinit var mediaObserver: MediaObserver

    private lateinit var observer: WhatsAppMediaObserver

    override fun onCreate() {
        super.onCreate()
        createNotification()

        observer = WhatsAppMediaObserver(this) { uri ->
            Log.d("Observer", "Nueva imagen detectada: $uri")
            // Aquí podrías copiar el archivo, subirlo a la nube, etc.
        }

        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            observer
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            mediaObserver.startWatching()
            Log.i("MediaObserverService", "MediaObserver started")
        } catch (e: Exception) {
            Log.e("MediaObserverService", "Failed to start MediaObserver", e)
        }

        return START_STICKY // Reinicia si el sistema lo elimina
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(observer)
    }


    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: Notification = NotificationCompat.Builder(this, "mediaObserver")
            .setContentTitle("Media Observer")
            .setContentText("Observando nuevas imágenes de WhatsApp")
            .setSmallIcon(R.drawable.ic_delete)
            .setContentIntent(pendingIntent)
            .setTicker("Observando nuevos archivos multimedia")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()

        startForeground(1337, notification)
    }

    private fun initMediaObserver() {
        mediaObserver = MediaObserver()
    }
}

package com.example.chatranslate

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.io.File
import java.text.DateFormat
import java.util.*

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return

        val packageName = sbn.packageName
        val extras = sbn.notification.extras

        val sender = extras.getString("android.title")?.trim()
        val message = extras.getCharSequence("android.text")?.toString()?.trim()
        val timestamp = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date())

        if (sender.isNullOrEmpty() || message.isNullOrEmpty()) return

        val logEntry = "$timestamp | $sender: $message\n"

        when (packageName) {
            "com.whatsapp" -> writeToLogFile("msgLog.txt", logEntry)
            "org.thoughtcrime.securesms" -> writeToLogFile("signalMsgLog.txt", logEntry)
        }
    }

    private fun writeToLogFile(fileName: String, logEntry: String) {
        try {
            File(filesDir, fileName).appendText(logEntry)
        } catch (e: Exception) {
            Log.e("NotificationListener", "Error writing to log file $fileName", e)
        }
    }
}

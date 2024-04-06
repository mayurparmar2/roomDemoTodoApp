package com.demo.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        showNotification(context, "Notification Title", "Notification Content")
    }
    private fun showNotification(context: Context?, title: String, content: String) {
        val builder = NotificationCompat.Builder(context!!, "channel_id")
            .setSmallIcon(R.drawable.icon200)
            .setContentTitle("Title")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, builder.build())
    }




}
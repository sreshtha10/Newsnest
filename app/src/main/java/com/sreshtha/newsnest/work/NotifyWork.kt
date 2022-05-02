package com.sreshtha.newsnest.work

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.utils.Constants.CHANNEL_ID
import com.sreshtha.newsnest.utils.Constants.NOTIFICATION_ID_TAG


class NotifyWork(context: Context, parameters: WorkerParameters): Worker(context,parameters){

    override fun doWork(): Result {
        val id = inputData.getLong(NOTIFICATION_ID_TAG,0).toInt()
        sendNotification(id)
        return Result.success()
    }

    private fun sendNotification(id:Int){
        val intent = Intent(applicationContext,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID_TAG,id)

        val pendingIntent: PendingIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            null
        }

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val title = "Newsnest"
        val subtitleNotification = "Check out some latest news. A lot has happened since you left."

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(subtitleNotification)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        if(pendingIntent!=null){
            builder.setContentIntent(pendingIntent)
        }

        notificationManager.notify(id, builder.build())

    }

}
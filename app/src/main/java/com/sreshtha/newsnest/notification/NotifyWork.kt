package com.sreshtha.newsnest.notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sreshtha.newsnest.R
import com.sreshtha.newsnest.ui.MainActivity
import com.sreshtha.newsnest.utils.Constants.CHANNEL_ID
import com.sreshtha.newsnest.utils.Constants.NOTIFICATION_ID_TAG


class NotifyWork(context: Context, parameters: WorkerParameters): Worker(context,parameters){

    companion object{

    }

    override fun doWork(): Result {
        val id = inputData.getLong(NOTIFICATION_ID_TAG,0).toInt()
        sendNotification(id)
        return Result.success()
    }

    private fun sendNotification(id:Int){
        val intent = Intent(applicationContext,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID_TAG,id)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val title = "Notification Title"
        val subtitleNotification = "Subtitle Notification"

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favourite)
            .setContentTitle(title)
            .setContentText(subtitleNotification)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        notificationManager.notify(id, builder.build())

    }

}
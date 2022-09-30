package com.google.firebase.example.messaging.kotlin

//import androidx.work.OneTimeWorkRequest
//import androidx.work.WorkManager
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//import com.google.firebase.example.messaging.R

import DBManager
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import com.nabard.sarnevesht.MainActivity
import com.nabard.sarnevesht.MyNotificationPublisher
import com.nabard.sarnevesht.R
import java.text.SimpleDateFormat

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")
        val notificationManager =
            baseContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel("high", "high", importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            if(remoteMessage.data["contenturl"]?.isNotEmpty() == true) {
                val sharedPreference =
                    baseContext.getSharedPreferences("content", Context.MODE_PRIVATE)
               val editor= sharedPreference.edit()
                editor.putString("url",remoteMessage.data["contenturl"]!!)
                editor.apply()
                return
            }
            else if(remoteMessage.data["url"]?.isNotEmpty() == true)
            {
                try {
                    val mBuilder = NotificationCompat.Builder(applicationContext, "high")
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(remoteMessage.data["url"])

                    val pendingIntent = PendingIntent.getActivity(applicationContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)

                    val bigText =
                        NotificationCompat.BigTextStyle()
                    mBuilder.setContentIntent(pendingIntent)
                    mBuilder.setContentTitle(remoteMessage.data["title"])
                    mBuilder.setContentText(remoteMessage.data["body"])
                    mBuilder.priority = NotificationCompat.PRIORITY_MAX
                    mBuilder.setAutoCancel(true)
                    mBuilder.setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    notificationManager.notify(0,mBuilder.build())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return
            }

            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date=dateFormat.parse(remoteMessage.data["date"])
            val dbManager = DBManager(baseContext)
            dbManager.open()
            dbManager.insert(remoteMessage.data["title"],remoteMessage.data["body"],remoteMessage.data["picture"],date,remoteMessage.data["tag"])
            dbManager.close()

           sendNotification(remoteMessage.data["title"],remoteMessage.data["body"],date)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

//    private fun scheduleJob() {
//        // [START dispatch_job]
//        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
//            .build()
//        WorkManager.getInstance(this)
//            .beginWith(work)
//            .enqueue()
//        // [END dispatch_job]
//    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Firebase.messaging.subscribeToTopic("all")
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(title:String?, body: String?,date:java.util.Date) {
        val intent = Intent(baseContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var builder = NotificationCompat.Builder(baseContext,"high")
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(title)
            .setContentText(body)
            .setSound( alarmSound)
            .setVibrate(longArrayOf( 1000, 1000, 1000, 1000, 1000 ))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        //.setWhen(System.currentTimeMillis()+100000)
//        with(NotificationManagerCompat.from(this.requireContext())) {
//            // notificationId is a unique int for each notification that you must define
//            notify(1, builder.build())
//        }
        scheduleNotification(builder.build(),date.time)
    }
    private fun scheduleNotification(notification: Notification, delay: Long) {
        val notificationIntent = Intent(baseContext, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            baseContext,1,notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = (baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)!!
       val resp= alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent)
    }
    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

//    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
//        override fun doWork(): Result {
//            // TODO(developer): add long running task here.
//            return Result.success()
//        }
//    }
}
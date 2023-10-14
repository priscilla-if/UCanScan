package nz.ac.uclive.dsi61.ucanscan

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class HurryUpAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("notifications", "Received race reminder")

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WAKE_LOCK
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WAKE_LOCK),
                AlarmReceiver.MY_PERMISSIONS_REQUEST_WAKE_LOCK
            )
            return
        }
        showHurryUpNotification(context)
    }

    private fun showHurryUpNotification(context: Context) {
        Log.d("notifications", "showing notification!")

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WAKE_LOCK
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WAKE_LOCK),
                AlarmReceiver.MY_PERMISSIONS_REQUEST_WAKE_LOCK
            )
            return
        }

        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.camera) //TODO change this to app icon
            .setContentTitle("Hurry up!")
            .setContentText("Time is ticking")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)




        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Race Reminder Channel"
            val descriptionText = "Channel for race reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "RaceReminderChannel"
        private const val NOTIFICATION_ID = 2
   }
}

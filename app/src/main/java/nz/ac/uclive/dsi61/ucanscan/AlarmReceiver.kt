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

class AlarmReceiver : BroadcastReceiver() {

    private val factsOfTheDay = listOf<String>(
        "UC was founded in 1873: we celebrated our 150th anniversary in 2023!",
        "Many of our buildings are named after New Zealanders who studied at UC!",
        "Our central library has 11 floors but you can only get a seat if you arrive before 9am!",
        "UC has over 180 student-run clubs: find out about them on Clubs Day at the start of the semester!",
        "UC is ranked in the top 250 universities worldwide for Engineering & Technology!",
        "Jack Erskine is the coolest building on campus, because the rooms are lined with bare concrete!",
        "Behind Jack Erskine is a garden representing different math concepts: mainly the famous \"Königsberg bridges\" problem!",
        "UC has 2 community gardens, on the Ilam & Dovedale campuses: you can grow your own food here!",
        "K1 lecture theatre is so far away, that walking over there gives you as much exercise as a session in the Rec Centre!",
        "The UCSA has an event at least once a week: check out ucsa.org.nz/whatson!",
        "The biggest lecture theatre is C1: it seats *400* people!",
        "Erskine's Re:Boot cafe was closed in 2023, after more than 20 years of making coffee machine echoes in the building 💔",
        "A path near South Arts and Meremere is lined with cherry trees: come at the start of September to see them in bloom!",
        "There are lemon and lime trees planted outside Len Lye on Science Road!",
        "There are many unofficial Facebook groups & Discord servers made by and for UC students!",
        "UC has 3 libraries: PJH is the central one, EPS is the science one, and Macmillan Brown is the cultural heritage one!",
        "The Maker Space on Level 2 of the central library lets you craft all sorts of things: for example it has a badge maker!",
        "There are some \"Free Tables\" on campus, for example under the stairs of the main library: donate & pick up stuff here!",
        "At the UBS, you can buy all sorts of UC alumni merch, such as expensive degree frames!",
        "Students with a Community Services card can get free appointments at the Health Centre!",
        "You can pick up a Tertiary Student bus card from the UCSA to make your Metro bus fare only $1!",
        "Ilam Fire Station is located on-campus, next to the engineering facilities!",
        "The heaters in the Macmillan Brown discussion rooms are filled with asbestos: don't open them!",
    )


    override fun onReceive(context: Context, intent: Intent) {
        Log.d("notifications", "received alarm")

        // Show a notification with the desired text
        showNotification(context, "hello hi fun fact")
    }

    private fun showNotification(context: Context, text: String) {
        Log.d("notifications", "showing notification!")

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WAKE_LOCK
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WAKE_LOCK),
                MY_PERMISSIONS_REQUEST_WAKE_LOCK
            )
            return
        }

        createNotificationChannel(context)
        val factText = factsOfTheDay.random()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.camera) //TODO change this to app icon
            .setContentTitle("UC Fact of the Day")
            .setContentText(factText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)




        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily Notification Channel"
            val descriptionText = "Channel for daily notifications"
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
        private const val CHANNEL_ID = "DailyNotificationChannel"
        private const val NOTIFICATION_ID = 1
        const val MY_PERMISSIONS_REQUEST_WAKE_LOCK = 456
    }
}




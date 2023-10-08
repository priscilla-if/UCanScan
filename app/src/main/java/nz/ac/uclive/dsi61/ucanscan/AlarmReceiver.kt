package nz.ac.uclive.dsi61.ucanscan

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {

    private val factsOfTheDay = listOf<String>(
        "UC was founded in 1873: we celebrated our 150th anniversary in 2023!",
        "Many of our buildings are named after New Zealanders who studied at UC!",
        "Our central library has 11 floors but you can only get a seat if you arrive before 9am!",
        "UC has over 180 student-run clubs: find out about them on Clubs Day at the start of the semester!",
        "UC is ranked in the top 250 universities worldwide for Engineering and Technology!",
        "Jack Erskine is the coolest building on campus, because the rooms are lined with bare concrete!",
        "Behind Jack Erskine is a garden that embodies different math problems: mainly the famous \"Königsberg bridges\" problem!",
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
        Log.d("FOO", "AlarmReceiver onReceive!")




//        val calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            add(Calendar.DAY_OF_YEAR, 1) // Add 1 day to get the next day
//        }

//        val intent = Intent(context, AlarmReceiver::class.java).let {
//            PendingIntent.getBroadcast(context, 0, it, 0)
//        }
//
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, intent)





        // Open the app when the notification is tapped
        val intent = Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        // Create the notification with a randomly selected fact about UC
        val factText = factsOfTheDay.random()
        val notification = Notification.Builder(context, Notification.CATEGORY_REMINDER).run {
            setSmallIcon(R.drawable.camera) //TODO: change image to main app icon
            setContentTitle("UC Fact of the Day")
            setContentText(factText)    // this is the text when the notif is minimised
                .style = Notification.BigTextStyle() // make the notification take up multiple lines
                .bigText(factText)      // this is the text when the notif is expanded
            setContentIntent(intent)
            setAutoCancel(true)
            build()
        }



//
//
//        // Schedule the next alarm for the next day at midnight
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            pendingIntent
//        )
//
//
//



        // Send the notification
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, notification)
//
//        //TODO: daily notifications
//        //TODO: why do the notifs change like every 3 seconds
//


    }


}
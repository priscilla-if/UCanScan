package nz.ac.uclive.dsi61.ucanscan

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.dsi61.ucanscan.navigation.NavGraph
import nz.ac.uclive.dsi61.ucanscan.ui.theme.UCanScanTheme
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel
import java.util.Calendar

class MainActivity : ComponentActivity() {
    private val AlarmReceiver = AlarmReceiver()

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UCanScanTheme(content = {
                Scaffold(
                ) {
                    val navController = rememberNavController()
                    val stopwatchViewModel = remember { StopwatchViewModel() }
                    val isRaceStartedModel: IsRaceStartedModel = viewModel()

                    NavGraph(navController = navController, stopwatchViewModel = stopwatchViewModel, isRaceStartedModel = isRaceStartedModel
                    )
                }
            })
        }

        createNotificationChannel()
    }


    override fun onStart() {
        super.onStart()
        Log.d("FOO", "MainActivity started!")
        registerReceiver(AlarmReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onStop() {
        super.onStop()
        Log.d("FOO", "MainActivity stopped!")
        unregisterReceiver(AlarmReceiver)
    }



    private fun createNotificationChannel() {
        Log.d("FOO", "MainActivity createNotificationChannel!")
        val importance = NotificationManager.IMPORTANCE_DEFAULT //TODO: change?
        val channel = NotificationChannel(Notification.CATEGORY_REMINDER, "UC Fact of the Day", importance).apply {
            description = "Daily facts and tips about UC"
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }




    // STEP 8
//    override fun onTimeSet(picker: TimePicker, hour: Int, minute: Int) {
//        val today = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, hour)
//            set(Calendar.MINUTE, minute)
//        }
//
//        val intent = Intent(applicationContext, AlarmReceiver::class.java).let {
//            PendingIntent.getBroadcast(applicationContext, 0, it, 0)
//        }
//
//        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setInexactRepeating(AlarmManager.RTC, today.timeInMillis, AlarmManager.INTERVAL_DAY, intent)

//        // STEP 11
//        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
//        prefs.edit().apply {
//            putInt("hour", hour)
//            putInt("minute", minute)
//            apply()
//        }
        //Utilities.scheduleReminder(applicationContext, hour, minute)
        // Calculate the time for the next day at midnight
//        val calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            add(Calendar.DAY_OF_YEAR, 1) // Add 1 day to get the next day
//        }
//
//        val intent = Intent(context, AlarmReceiver::class.java).let {
//            PendingIntent.getBroadcast(context, 0, it, 0)
//        }
//
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setInexactRepeating(AlarmManager.RTC, today.timeInMillis, AlarmManager.INTERVAL_DAY, intent)
//
//


//        // STEP 13: set the boot receiver to enabled
//        val receiver = ComponentName(this, BootReceiver::class.java)
//        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    //}




    //TODO: setting to set what hour the daily notif comes at

}

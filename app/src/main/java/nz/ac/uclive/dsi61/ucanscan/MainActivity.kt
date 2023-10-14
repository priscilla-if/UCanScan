package nz.ac.uclive.dsi61.ucanscan

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.dsi61.ucanscan.navigation.NavGraph
import nz.ac.uclive.dsi61.ucanscan.ui.theme.UCanScanTheme
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.PreferencesViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.PreferencesViewModelFactory
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel
import java.util.Calendar

class MainActivity : ComponentActivity() {

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
                    val context = LocalContext.current
                    val application = context.applicationContext as UCanScanApplication
                    val repository = application.repository
                    val landmarkViewModel: LandmarkViewModel = remember { LandmarkViewModel(repository) }
                    val preferencesViewModel: PreferencesViewModel = viewModel(
                        factory = PreferencesViewModelFactory(application.repository)
                    )
                    NavGraph(navController = navController, stopwatchViewModel = stopwatchViewModel, isRaceStartedModel = isRaceStartedModel, preferencesViewModel = preferencesViewModel, landmarkViewModel)



                }
            })
        }

    }


    override fun onStart() {
        super.onStart()
        Log.d("FOO", "MainActivity started!")
        scheduleReminder()
    }

    override fun onStop() {
        super.onStop()
        Log.d("FOO", "MainActivity stopped!")
    }


    private fun scheduleReminder() {

        Log.d("notifications", "setting up alarm")

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        //test notifs for 30 seconds from now (not exactly 30 seconds though)
    /*    alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            30 * 1000,  // 30 seconds in milliseconds
            alarmIntent
        )*/


        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }



/*    private fun scheduleHurryUpReminder(context: Context) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val raceReminderIntent = Intent(this, HurryUpAlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val triggerTime = System.currentTimeMillis() + 30 * 60 * 1000
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            raceReminderIntent
        )
    }*/



}

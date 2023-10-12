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
    private val AlarmReceiver = AlarmReceiver()

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UCanScanTheme(content = {
                Scaffold(
                ) {
//                    val application = applicationContext as UCanScanApplication

                    val navController = rememberNavController()
                    val stopwatchViewModel = remember { StopwatchViewModel() }
                    val isRaceStartedModel: IsRaceStartedModel = viewModel()
                    NavGraph(navController = navController, stopwatchViewModel = stopwatchViewModel, isRaceStartedModel = isRaceStartedModel, preferencesViewModel = preferencesViewModel, landmarkViewModel)
                    val context = LocalContext.current

                    val application = context.applicationContext as UCanScanApplication
                    val repository = application.repository
                    val landmarkViewModel: LandmarkViewModel = remember { LandmarkViewModel(repository) }
                    val preferencesViewModel: PreferencesViewModel = viewModel(
                        factory = PreferencesViewModelFactory(application.repository)
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


}

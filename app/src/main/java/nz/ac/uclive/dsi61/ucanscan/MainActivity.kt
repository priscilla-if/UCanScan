package nz.ac.uclive.dsi61.ucanscan

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
                    val navController = rememberNavController()
                    val stopwatchViewModel = remember { StopwatchViewModel() }
                    val isRaceStartedModel: IsRaceStartedModel = viewModel()
                    val context = LocalContext.current

                    val application = context.applicationContext as UCanScanApplication

                    val preferencesViewModel: PreferencesViewModel = viewModel(
                        factory = PreferencesViewModelFactory(application.repository)
                    )

                    NavGraph(navController = navController, stopwatchViewModel = stopwatchViewModel, isRaceStartedModel = isRaceStartedModel, preferencesViewModel = preferencesViewModel
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
        val factChannel = NotificationChannel(
            "fact_channel",
            "UC Fact of the Day",
            importance
        ).apply {
            description = "Daily facts and tips about UC"
        }

        val hurryUpChannel = NotificationChannel(
            "hurryup_channel",
            "Hurry Up Notification",
            importance
        ).apply {
            description = "Hurry Up Reminder"
        }

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(factChannel)
        notificationManager.createNotificationChannel(hurryUpChannel)
    }


}

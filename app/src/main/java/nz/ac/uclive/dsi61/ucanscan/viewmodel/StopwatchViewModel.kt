package nz.ac.uclive.dsi61.ucanscan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class StopwatchViewModel : ViewModel() {
    var isRunning: Boolean by mutableStateOf(false)
    var time: Long by mutableStateOf(0L)
    var startTime: Long by mutableStateOf(0L)
}
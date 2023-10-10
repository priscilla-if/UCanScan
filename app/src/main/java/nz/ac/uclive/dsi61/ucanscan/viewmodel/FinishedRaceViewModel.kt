package nz.ac.uclive.dsi61.ucanscan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nz.ac.uclive.dsi61.ucanscan.entity.Times
import nz.ac.uclive.dsi61.ucanscan.repository.UCanScanRepository

class FinishedRaceViewModel(private val repository: UCanScanRepository) : ViewModel() {


    fun addTime(time: Times) {
        viewModelScope.launch {
            repository.addTime(time)
        }
    }


    private val allTimesFlow = MutableStateFlow<List<Times>>(emptyList())
    val allTimes: StateFlow<List<Times>> get() = allTimesFlow

    init {
        viewModelScope.launch {
            repository.allTimes.collect {
                allTimesFlow.value = it
            }
        }
    }

}
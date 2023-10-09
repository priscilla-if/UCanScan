package nz.ac.uclive.dsi61.ucanscan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.uclive.dsi61.ucanscan.entity.Times
import nz.ac.uclive.dsi61.ucanscan.repository.UCanScanRepository

class FinishedRaceViewModel(private val repository: UCanScanRepository) : ViewModel() {

    fun addTime(time: Times) {
        viewModelScope.launch {
            repository.addTime(time)
        }
    }
}
package nz.ac.uclive.dsi61.ucanscan.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import nz.ac.uclive.dsi61.ucanscan.entity.Preferences
import nz.ac.uclive.dsi61.ucanscan.repository.UCanScanRepository


class PreferencesViewModel(private val repository: UCanScanRepository) : ViewModel() {

    @Composable
    fun getPreferenceState(preferenceName: String): State<Boolean> =
        repository.getPreferenceState(preferenceName).collectAsState(initial = true)

    fun updatePreferenceState(preferenceName: String, newValue: Boolean) {
        viewModelScope.launch {
            repository.updatePreference(preferenceName, newValue)
        }
    }
}


class PreferencesViewModelFactory(private val repository: UCanScanRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferencesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreferencesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
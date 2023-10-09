package nz.ac.uclive.dsi61.ucanscan.viewmodel

import androidx.lifecycle.ViewModel
import nz.ac.uclive.dsi61.ucanscan.repository.UCanScanRepository

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PreferencesViewModel(private val repository: UCanScanRepository) : ViewModel() {

   /* fun getPreferenceStateByName(name: String): Boolean {
        return repository.getPreferenceByName(name)
    }*/

    fun getPreferenceStateByName(name: String): Flow<Boolean> {
        return repository.getPreferenceByName(name)
    }


 /*   fun updatePreferenceState(name: String, newState: Boolean) {
        viewModelScope.launch {
            repository.updatePreference(name, newState)
        }
    }*/

    fun updatePreferenceState(name: String, newState: Boolean) {
        viewModelScope.launch {
            repository.updatePreference(name, newState)
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


package nz.ac.uclive.dsi61.ucanscan.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.dsi61.ucanscan.entities.Landmark
import nz.ac.uclive.dsi61.ucanscan.repository.UCanScanRepository

class LandmarkViewModel(private val repository: UCanScanRepository) : ViewModel() {

    // Define a function to get the pre-filled landmarks from the database
    fun getLandmarks(): Flow<List<Landmark>> {
        return repository.landmarks;
    }
}

class LandmarkViewModelFactory(private val repository: UCanScanRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandmarkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LandmarkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


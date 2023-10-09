package nz.ac.uclive.dsi61.ucanscan.viewmodel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class IsRaceStartedModel : ViewModel() {
    private val _isRaceStarted = mutableStateOf(false)
    val isRaceStarted: State<Boolean> = _isRaceStarted

    fun setRaceStarted(value: Boolean) {
        _isRaceStarted.value = value
    }
}
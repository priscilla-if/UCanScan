package nz.ac.uclive.dsi61.ucanscan

import android.os.Bundle
import androidx.compose.runtime.saveable.Saver
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark

fun LandmarkSaver(): Saver<Landmark, *> = Saver(
    //create bundle for landmark

    save = { landmark ->
        val bundle = Bundle()
        bundle.putString("name", landmark.name)
        bundle.putString("description", landmark.description)
        bundle.putDouble("latitude", landmark.latitude)
        bundle.putDouble("longitude", landmark.longitude)
        bundle.putBoolean("isFound", landmark.isFound)
        bundle
    },
    restore = { savedValue ->
        //turn bundle into landmark

        Landmark(
            savedValue.getString("name") ?: "",
            savedValue.getString("description") ?: "",
            savedValue.getDouble("latitude"),
            savedValue.getDouble("longitude"),
            savedValue.getBoolean("isFound")
        )
    }
)


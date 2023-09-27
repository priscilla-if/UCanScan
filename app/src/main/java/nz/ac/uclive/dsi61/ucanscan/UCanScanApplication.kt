package nz.ac.uclive.dsi61.ucanscan;

import nz.ac.uclive.dsi61.ucanscan.database.UCanScanDatabase
import android.app.Application;
import nz.ac.uclive.dsi61.ucanscan.repository.UCanScanRepository

// Application class that initialises our instance of the DB
class UCanScanApplication : Application() {
        val database by lazy { UCanScanDatabase.getDatabase(this) }
        val repository by lazy { UCanScanRepository(database) }
        }

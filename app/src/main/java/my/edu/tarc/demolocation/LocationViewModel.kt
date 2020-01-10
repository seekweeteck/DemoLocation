package my.edu.tarc.demolocation

import android.location.Location
import androidx.lifecycle.ViewModel

class LocationViewModel  : ViewModel(){
    private var _location : Location? = null

    fun setLocation(location: Location){
        _location = location
    }

    fun getLocation(): Location? {
        return _location
    }
}
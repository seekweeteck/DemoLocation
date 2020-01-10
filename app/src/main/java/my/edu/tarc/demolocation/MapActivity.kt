package my.edu.tarc.demolocation

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var myGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myGoogleMap = googleMap

        //Set a preference for min and max zoom
        myGoogleMap.setMinZoomPreference(6.0f)
        myGoogleMap.setMaxZoomPreference((14.0f))

        //Set a location
        val lat = intent.getDoubleExtra(MainActivity.LAT, 0.0)
        val lon = intent.getDoubleExtra(MainActivity.LON, 0.0)
        val kualalumpur = LatLng(lat, lon)

        //Set a camera view
        val cameraPosition = CameraPosition.builder().run {
            target(kualalumpur)
            zoom(30f)
        }.build()

        // Add a marker in Kuala Lumpur and move the camera
        myGoogleMap.addMarker(MarkerOptions().position(kualalumpur).title("Marker in Kuala Lumpur"))
        //myGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(kualalumpur))
        myGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}

package my.edu.tarc.demolocation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationViewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize Fused Location Client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        //Initialize View Model
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        buttonShowMapIntent.setOnClickListener {
            showMapIntent()
        }

        buttonShowMapView.setOnClickListener {
            showMapView()
        }

        buttonShowMapIntent.isEnabled = false
        buttonShowMapView.isEnabled= false

        checkPermission()
    }



    private fun checkPermission() {
        val permission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestPermission()
        } else {
            showLocation()
        }
    }

    fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                showLocation()
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                textViewLocation.text = String.format(
                    "%s : %s",
                    getString(R.string.result),
                    getString(R.string.no_location)
                )
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showLocation() {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                if (it != null) {
                    textViewLocation.text =
                        String.format("Lat: %.4f\nLon : %.4f", it.latitude, it.longitude)

                    locationViewModel.setLocation(it)

                    buttonShowMapIntent.isEnabled = true
                    buttonShowMapView.isEnabled = true
                } else {
                    textViewLocation.text = String.format("%s", getString(R.string.error_location))
                }
            }
    }

    private fun showMapIntent() {
        val urlMap = Uri.parse("geo:"
            .plus( locationViewModel.getLocation()?.latitude.toString())
            .plus(",")
            .plus(locationViewModel.getLocation()?.longitude.toString()))

        val intent = Intent(Intent.ACTION_VIEW, urlMap)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(applicationContext, getString(R.string.error_intent), Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun showMapView() {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra(LAT, locationViewModel.getLocation()?.latitude)
        intent.putExtra(LON, locationViewModel.getLocation()?.longitude)
        startActivity(intent)
    }

    companion object {
        val REQUEST_LOCATION = 1
        val LAT = "my.edu.tarc.demolocation.LAT"
        val LON = "my.edu.tarc.demolocation.LON"
    }
}

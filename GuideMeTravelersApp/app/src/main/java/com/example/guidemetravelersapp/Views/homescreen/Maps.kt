package com.example.guidemetravelersapp.Views.homescreen

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.guidemetravelersapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Maps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        createFragment()
    }

    private fun createFragment() {
        val mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        enableLocation()
    }

    // adds a marker to the map, with zoom animation to the marker
    private fun createMarker() {
        val coordinates = LatLng(18.484234720535127, -69.93869661632442)
        val marker = MarkerOptions().position(coordinates).title("Agora Mall")
        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 18f), 4000, null)
    }

    // condition to know when the location permission has been granted
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation() {
        if(!::map.isInitialized) return // when the map is not initialized
        if(isLocationPermissionGranted()){
            // uncomment to see real-time location in map
            //map.isMyLocationEnabled = true // real time location, app runs error can be ignored
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        // user deny the request
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Go to Settings and enable the location", Toast.LENGTH_SHORT).show()
        } else {
            // user accepts the request
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    // captures user's acceptance of the permission (built-in function)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            // users have accepted the request
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // uncomment to see real-time location on map
                //map.isMyLocationEnabled = true
            } else {
                Toast.makeText(this, "Go to Settings and enable the location permission", Toast.LENGTH_SHORT).show()
            }
            else -> { }
        }
    }

    // if permissions have been removed (built-in function)
    override fun onResumeFragments() {
        super.onResumeFragments()
        if(!::map.isInitialized) return // when the map is not initialized
        if(!isLocationPermissionGranted()) {
            // uncomment to see real-time location on map
            //map.isMyLocationEnabled = false
            Toast.makeText(this, "Go to Settings and enable the location permission", Toast.LENGTH_SHORT).show()
        }
    }
}
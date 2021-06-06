package com.eseo.getmyspot.view.main.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.BatteryManager
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eseo.getmyspot.BuildConfig
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.preferences.LocalPreferences
import com.eseo.getmyspot.view.account.signin.SigninActivity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


class AddSpotFragment : Fragment(), SensorEventListener {

    companion object {
        fun newInstance() = AddSpotFragment()
    }

    private var imageBase64: String? = null
    private val PICK_IMAGE = 9999
    private val PERMISSION_REQUEST_LOCATION = 9999
    private var sensorPressure: Sensor? = null
    private var sensorLight: Sensor? = null
    private var sensorManager: SensorManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorPressure = sensorManager!!.getDefaultSensor(Sensor.TYPE_PRESSURE)
        sensorLight = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_spot, container, false)
    }

    override fun onResume() {
        super.onResume()
        setUpUI()
        if (LocalPreferences.getInstance(requireContext())
                .getStringValue(LocalPreferences.PSEUDO) == null
        ) {
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.require_connection_title))
                .setCancelable(false)
                .setMessage(R.string.require_connection_message)
                .setPositiveButton(getString(R.string.log_in)) { dialog, which ->
                    startActivity(SigninActivity.getStartIntent(requireContext()))
                }.setNegativeButton(getText(R.string.cancel)) { dialog, which ->
                    findNavController().navigate(R.id.action_add_spot_to_home)
                }
                .show()
        } else {
            askPermission()
        }
    }

    private fun setUpUI() {
        this.view?.findViewById<Button>(R.id.btn_gallery)?.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, PICK_IMAGE)
        }
    }

    private fun displaySpotImage() {
        this.view?.findViewById<ImageView>(R.id.image_spot)
            ?.setImageBitmap(decodeToBitMap(imageBase64))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                val imageStream: InputStream? =
                    data.getData()?.let { requireContext().contentResolver.openInputStream(it) }
                val yourSelectedImage = BitmapFactory.decodeStream(imageStream)
                imageBase64 = encodeTobase64(yourSelectedImage)
                if (imageBase64 != null)
                    displaySpotImage()
            }
        }
    }

    private fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.getEncoder().encodeToString(b)
        Log.e("LOOK", imageEncoded)
        return imageEncoded
    }

    private fun decodeToBitMap(image: String?): Bitmap? {
        if (image == null)
            return null
        val decodedString: ByteArray = Base64.getDecoder().decode(image)
        val bitmapImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return bitmapImage
    }

    private fun askPermission() {
        if (!hasPermissions()) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                MaterialAlertDialogBuilder(requireContext())
                    .setCancelable(false)
                    .setTitle(resources.getString(R.string.dialog_title_permission))
                    .setMessage(resources.getString(R.string.dialog_message_permission))
                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                            )
                        )
                    }
                    .setNegativeButton(R.string.refuse) { dialog, which ->
                        findNavController().navigate(R.id.action_add_spot_to_home)
                    }
                    .show()
            } else {
                requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    PERMISSION_REQUEST_LOCATION
                )
            }
        } else {
            getSensorValues()
        }
    }

    private fun getSensorValues() {
        getLocalisation()
        getBatteryPourcentage()
        sensorManager!!.registerListener(this, sensorPressure, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager!!.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun hasPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocalisation() {
        if (hasPermissions()) {
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            )
                .addOnSuccessListener { geoCode(it) }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Localisation impossible", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun geoCode(location: Location) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val results = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (results.isNotEmpty()) {
            this.view?.findViewById<TextView>(R.id.txt_address)?.text =
                " " + results[0].getAddressLine(0)
        }
    }


    private fun getBatteryPourcentage() {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context?.registerReceiver(null, ifilter)
        }
        val batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
        this.view?.findViewById<TextView>(R.id.txt_battery)?.text = batteryPct.toString() + " " + getString(R.string.pourcentage)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_PRESSURE) {
                this.view?.findViewById<TextView>(R.id.txt_altitude)?.text =
                    SensorManager.getAltitude(
                        SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
                        event.values[0]
                    ).toString() + " " + getString(R.string.metter)
                this.view?.findViewById<TextView>(R.id.txt_pressure)?.text =
                    event.values[0].toString() + " " + getString(R.string.mbar)
            } else if (event.sensor.type == Sensor.TYPE_LIGHT) {
                this.view?.findViewById<TextView>(R.id.txt_light)?.text =
                    event.values[0].toString() + " " + getString(R.string.lux)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}
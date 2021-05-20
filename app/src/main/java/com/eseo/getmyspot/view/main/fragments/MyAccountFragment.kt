package com.eseo.getmyspot.view.main.fragments

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.models.SpotModel
import com.eseo.getmyspot.view.main.adapter.SpotsAdapter
import com.eseo.getmyspot.view.settings.SettingsActivity
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime


class MyAccountFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onResume() {
        super.onResume()
        view?.apply {
            findViewById<ImageView>(R.id.settings)?.setOnClickListener {
                startActivity(SettingsActivity.getStartIntent(requireContext()))
            }

            findViewById<ImageView>(R.id.profile_picture).setOnClickListener {
                openGalleryForImage()
            }
            findViewById<RecyclerView>(R.id.rvMySpots).layoutManager = LinearLayoutManager(requireContext())
            findViewById<RecyclerView>(R.id.rvMySpots).setNestedScrollingEnabled(false);
            var position = Location("position")
            position.longitude = 41.0
            position.latitude = 41.0
            findViewById<RecyclerView>(R.id.rvMySpots).adapter = SpotsAdapter(arrayOf()
            ) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:" + position.latitude + "," + position.longitude)
                    )
                )
            }
        }
    }

    companion object {
        fun newInstance() = MyAccountFragment()
        const val REQUEST_CODE = 999
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            view?.apply {
                val imageUri = data?.data
                if (imageUri != null) {
                    findViewById<ImageView>(R.id.profile_picture).setImageURI(imageUri) // handle chosen image
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);

                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, stream)
                    val byteArray: ByteArray = stream.toByteArray()

                    Base64.encodeToString(byteArray, Base64.DEFAULT)
                }
            }
        }
    }
}
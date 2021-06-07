package com.eseo.getmyspot.view.main.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.models.SpotModel
import com.eseo.getmyspot.data.preferences.LocalPreferences
import com.eseo.getmyspot.view.Failed
import com.eseo.getmyspot.view.main.adapter.AccountSpotsAdapter
import com.eseo.getmyspot.view.settings.SettingsActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class MyAccountFragment : Fragment() {

    private val myAccountProfilPictureViewModel: MyAccountProfilPictureViewModel by viewModel()
    private val myAccountSpotsViewModel: MyAccountSpotsViewModel by viewModel()
    private val content = mutableListOf<SpotModel>()
    private var pageCourante = 0
    private var pseudo : String? = null

    companion object {
        fun newInstance() = MyAccountFragment()
        const val REQUEST_CODE = 999
        const val NBELEMENTS = 10
    }

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
        pseudo = LocalPreferences.getInstance(requireContext()).getStringValue(LocalPreferences.PSEUDO)
        setupUi()
    }

    private fun setupUi() {
        pageCourante = 0
        view?.apply {
            findViewById<RecyclerView>(R.id.rvMySpots)?.adapter?.notifyDataSetChanged()
            findViewById<ImageView>(R.id.settings)?.setOnClickListener {
                startActivity(SettingsActivity.getStartIntent(requireContext()))
            }

            findViewById<Button>(R.id.more_data)?.setOnClickListener {
                pageCourante++
                myAccountSpotsViewModel.doRemoteAction(pseudo, pageCourante*NBELEMENTS, pageCourante*NBELEMENTS+NBELEMENTS)
            }

            findViewById<ImageView>(R.id.profile_picture).setOnClickListener {
                openGalleryForImage()
            }
            findViewById<RecyclerView>(R.id.rvMySpots).layoutManager = LinearLayoutManager(requireContext())
            findViewById<RecyclerView>(R.id.rvMySpots).setNestedScrollingEnabled(false);

            // get spots
            myAccountSpotsViewModel.doRemoteAction(pseudo, pageCourante*NBELEMENTS, pageCourante*NBELEMENTS+NBELEMENTS)

            findViewById<RecyclerView>(R.id.rvMySpots).adapter = AccountSpotsAdapter(content) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:" + it.position.latitude + "," + it.position.longitude)
                    )
                )
            }
        }

        myAccountProfilPictureViewModel.states.observe(this, Observer { state ->
            when (state) {
                //is Loading -> TODO : peut être mettre un logo de chargement plus tard
                is MyAccountProfilPictureViewModel.CallResult ->
                    if (state.isPictureProfileChange) {
                        Toast.makeText(requireContext(), "CHANGED", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "UNCHANGED", Toast.LENGTH_SHORT).show()
                    }
                is Failed -> Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        myAccountSpotsViewModel.states.observe(this, Observer { state ->
            when (state) {
                //is Loading -> TODO : peut être mettre un logo de chargement plus tard
                is MyAccountSpotsViewModel.CallResult ->
                    if (state.result.error == 0) {
                        content.clear()
                        state.result.list_spots.forEach {
                            var position = Location("")
                            position.latitude = it.position_latitude
                            position.longitude = it.position_longitude
                            val time = convertTime(it.time)
                            content.add(SpotModel(it.pseudo, it.image, it.battery, time, position,  it.pressure, it.brightness, it.image_spot))
                        }
                        this.view?.findViewById<RecyclerView>(R.id.rvMySpots)?.adapter?.notifyDataSetChanged()
                        Toast.makeText(requireContext(), "RECU", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "NON RECU", Toast.LENGTH_SHORT).show()
                    }
                is Failed -> Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun convertTime(time: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
        var date: Date? = null
        try {
            date = simpleDateFormat.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        if (date == null) {
            return ""
        }

        val convertDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return convertDateFormat.format(date)

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
                    val imgView = findViewById<ImageView>(R.id.profile_picture)
                    imgView.setImageURI(imageUri) // handle chosen image
                    val bitmap = (imgView.drawable as BitmapDrawable).bitmap
                    //val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, stream)
                    val byteArray: ByteArray = stream.toByteArray()

                    val pseudo = LocalPreferences.getInstance(requireContext()).getStringValue(LocalPreferences.PSEUDO)
                    if (pseudo != null) {
                        myAccountProfilPictureViewModel.doRemoteAction(pseudo, Base64.encodeToString(byteArray, Base64.DEFAULT))
                    }
                }
            }
        }
    }
}
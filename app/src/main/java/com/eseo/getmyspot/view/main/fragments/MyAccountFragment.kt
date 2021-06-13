package com.eseo.getmyspot.view.main.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.models.GetProfilePictureResult
import com.eseo.getmyspot.data.models.GetSpotsResult
import com.eseo.getmyspot.data.models.SpotModel
import com.eseo.getmyspot.data.preferences.LocalPreferences
import com.eseo.getmyspot.view.Failed
import com.eseo.getmyspot.view.account.signin.SigninActivity
import com.eseo.getmyspot.view.main.adapter.AccountSpotsAdapter
import com.eseo.getmyspot.view.settings.SettingsActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MyAccountFragment : Fragment() {

    private val myAccountProfilPictureViewModel: MyAccountProfilPictureViewModel by viewModel()
    private val myAccountSpotsViewModel: MyAccountSpotsViewModel by viewModel()
    private val myAccountGetProfilePictureViewModel: MyAccountGetProfilePictureViewModel by viewModel()
    private val content = mutableListOf<SpotModel>()
    private var pageCourante = 0
    private var pseudo: String? = null

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
        pseudo =
            LocalPreferences.getInstance(requireContext()).getStringValue(LocalPreferences.PSEUDO)
        setupUi()
    }

    private fun setupUi() {
        pageCourante = 0
        view?.apply {
            findViewById<ImageView>(R.id.settings)?.setOnClickListener {
                startActivity(SettingsActivity.getStartIntent(requireContext()))
            }

            // if user is connected
            if (pseudo != null) {
                findViewById<Button>(R.id.more_data).setVisibility(View.VISIBLE)
                findViewById<Button>(R.id.btn_connection).setVisibility(View.GONE)
                findViewById<TextView>(R.id.pseudo).text = pseudo

                // when click on button more : load more data with a API call
                findViewById<Button>(R.id.more_data).setOnClickListener {
                    pageCourante++
                    myAccountSpotsViewModel.doRemoteAction(
                        pseudo,
                        pageCourante * NBELEMENTS,
                        pageCourante * NBELEMENTS + NBELEMENTS,
                        ::onSpotsApiResult
                    )
                }

                // get the image profile
                myAccountGetProfilePictureViewModel.doRemoteAction(
                    pseudo!!,
                    ::onGetProfilePictureApiResult
                )

                findViewById<ImageView>(R.id.profile_picture).setOnClickListener {
                    openGalleryForImage()
                }
                findViewById<RecyclerView>(R.id.rvMySpots).layoutManager =
                    LinearLayoutManager(requireContext())
                findViewById<RecyclerView>(R.id.rvMySpots).setNestedScrollingEnabled(false);

                // clear the spots list before displayed it again
                content.clear()
                findViewById<RecyclerView>(R.id.rvMySpots)?.adapter?.notifyDataSetChanged()
                // get spots
                myAccountSpotsViewModel.doRemoteAction(
                    pseudo,
                    pageCourante * NBELEMENTS,
                    pageCourante * NBELEMENTS + NBELEMENTS,
                    ::onSpotsApiResult
                )

                findViewById<RecyclerView>(R.id.rvMySpots).adapter = AccountSpotsAdapter(content) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("geo:" + it.position.latitude + "," + it.position.longitude)
                        )
                    )
                }
            } else {
                findViewById<Button>(R.id.more_data).setVisibility(View.GONE)
                findViewById<Button>(R.id.btn_connection).setVisibility(View.VISIBLE)
                findViewById<Button>(R.id.btn_connection).setOnClickListener {
                    startActivity(SigninActivity.getStartIntent(requireContext()))
                }
            }
        }

        myAccountProfilPictureViewModel.states.observe(this, Observer { state ->
            when (state) {
                //is Loading -> TODO : peut être mettre un logo de chargement plus tard
                is MyAccountProfilPictureViewModel.CallResult ->
                    if (state.isPictureProfileChange) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.picture_profile_updated),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.picture_profile_not_updated),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                is Failed -> Toast.makeText(
                    requireContext(),
                    getString(R.string.picture_profile_not_updated),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun onGetProfilePictureApiResult(
        profilePictureResult: GetProfilePictureResult?,
        isError: Boolean
    ) {
        try {
            requireActivity().runOnUiThread {
                try {
                    if (!isError && profilePictureResult != null && profilePictureResult.image != null) {
                        val imageBytes = Base64.decode(profilePictureResult.image, 0)
                        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        this.view?.findViewById<ImageView>(R.id.profile_picture)
                            ?.setImageBitmap(image)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.picture_profile_not_exist),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } catch (err: Exception) {
                    System.err.println("On cherche à modifier une vue qui n'est plus affiché " + err)
                }
            }
        } catch (err: Exception) {
            System.err.println("On cherche à modifier une vue qui n'est plus affiché " + err)
        }
    }

    private fun onSpotsApiResult(spotsResult: GetSpotsResult?, isError: Boolean) {
        try {
            requireActivity().runOnUiThread {
                try {
                    if (!isError && spotsResult != null) {
                        if (spotsResult.list_spots != null) {
                            spotsResult.list_spots.forEach {
                                var position = Location("")
                                position.latitude = it.position_latitude
                                position.longitude = it.position_longitude
                                content.add(
                                    SpotModel(
                                        it.pseudo,
                                        it.image,
                                        it.battery,
                                        it.time,
                                        position,
                                        it.pressure,
                                        it.brightness,
                                        it.image_spot!!
                                    )
                                )
                            }
                            this.view?.findViewById<RecyclerView>(R.id.rvMySpots)?.adapter?.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.no_more_data),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.not_received),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (err: Exception) {
                    System.err.println("On cherche à modifier une vue qui n'est plus affiché " + err)
                }
            }
        } catch (err: Exception) {
            System.err.println("On cherche à modifier une vue qui n'est plus affiché " + err)
        }
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
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            view?.apply {
                val imageUri = data?.data
                if (imageUri != null) {
                    val imgView = findViewById<ImageView>(R.id.profile_picture)
                    imgView.setImageURI(imageUri) // handle chosen image
                    val bitmap = (imgView.drawable as BitmapDrawable).bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, stream)
                    val byteArray: ByteArray = stream.toByteArray()

                    val pseudo = LocalPreferences.getInstance(requireContext())
                        .getStringValue(LocalPreferences.PSEUDO)
                    if (pseudo != null) {
                        myAccountProfilPictureViewModel.doRemoteAction(
                            pseudo,
                            Base64.encodeToString(byteArray, Base64.DEFAULT)
                        )
                    }
                }
            }
        }
    }
}
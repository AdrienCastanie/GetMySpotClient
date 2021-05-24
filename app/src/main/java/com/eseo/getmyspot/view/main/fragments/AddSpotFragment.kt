package com.eseo.getmyspot.view.main.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.preferences.LocalPreferences
import com.eseo.getmyspot.view.account.signin.SigninActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


class AddSpotFragment : Fragment() {

    companion object {
        const val PICK_IMAGE = 8001

        fun newInstance() = AddSpotFragment()
    }

    private var imageBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_spot, container, false)
    }

    override fun onResume() {
        super.onResume()
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
        }
        setUpUI()
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
        this.view?.findViewById<ImageView>(R.id.image_spot)?.setImageBitmap(decodeToBitMap(imageBase64))
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
        if(image == null)
            return null
        val decodedString: ByteArray = Base64.getDecoder().decode(image)
        val bitmapImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return bitmapImage
    }
}
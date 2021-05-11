package com.eseo.getmyspot.view.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.preferences.LocalPreferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddSpotFragment : Fragment() {

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
        if (context?.let {
                LocalPreferences.getInstance(it).getStringValue(LocalPreferences.PSEUDO)
            } == null) {
            context?.let {
                MaterialAlertDialogBuilder(it).setTitle(getString(R.string.require_connection_title))
                    .setCancelable(false)
                    .setMessage(R.string.require_connection_message)
                    .setPositiveButton(getString(R.string.log_in)) { dialog, which ->
                        // TODO : faire la redirection vers l'activity de connection
                    }
                    .show()
            }
        }
    }

    companion object {
        fun newInstance() = AddSpotFragment()
    }
}
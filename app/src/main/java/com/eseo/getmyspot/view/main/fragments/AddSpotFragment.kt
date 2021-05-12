package com.eseo.getmyspot.view.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.preferences.LocalPreferences
import com.eseo.getmyspot.view.account.signin.SigninActivity
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
    }

    companion object {
        fun newInstance() = AddSpotFragment()
    }
}
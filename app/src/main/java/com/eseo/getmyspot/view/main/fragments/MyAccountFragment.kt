package com.eseo.getmyspot.view.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.eseo.getmyspot.R
import com.eseo.getmyspot.view.settings.SettingsActivity

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
        }
    }

    companion object {
        fun newInstance() = MyAccountFragment()
    }
}
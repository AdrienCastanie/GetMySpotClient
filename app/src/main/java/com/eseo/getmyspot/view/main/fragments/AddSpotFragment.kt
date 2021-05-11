package com.eseo.getmyspot.view.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eseo.getmyspot.R

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
        Toast.makeText(requireContext(), "Add_Spot", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = AddSpotFragment()
    }
}
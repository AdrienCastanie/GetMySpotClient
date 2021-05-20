package com.eseo.getmyspot.view.main.fragments

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.models.SpotModel
import com.eseo.getmyspot.view.main.adapter.SpotsAdapter
import com.eseo.getmyspot.view.settings.SettingsActivity
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

            findViewById<RecyclerView>(R.id.rvMySpots).layoutManager = LinearLayoutManager(requireContext())
            findViewById<RecyclerView>(R.id.rvMySpots).setNestedScrollingEnabled(false);
            var position = Location("position")
            position.longitude = 41.0
            position.latitude = 41.0
            findViewById<RecyclerView>(R.id.rvMySpots).adapter = SpotsAdapter(arrayOf(
                SpotModel("Adrien", null,"75", LocalDateTime.now(), position, "10", "1000", null),
                SpotModel("Adrien", null,"75", LocalDateTime.now(), position, "10", "1000", null),
                SpotModel("Adrien", null,"75", LocalDateTime.now(), position, "10", "1000", null),
                SpotModel("Adrien", null,"75", LocalDateTime.now(), position, "10", "1000", null))
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
    }
}
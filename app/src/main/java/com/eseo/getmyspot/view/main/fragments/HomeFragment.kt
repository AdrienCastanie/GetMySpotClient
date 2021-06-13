package com.eseo.getmyspot.view.main.fragments

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.models.GetSpotsResult
import com.eseo.getmyspot.data.models.SpotModel
import com.eseo.getmyspot.view.main.adapter.HomeSpotsAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private val homeSpotsViewModel: HomeSpotsViewModel by viewModel()
    private val content = mutableListOf<SpotModel>()
    private var pageCourante = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        setupUi()
    }

    private fun setupUi() {
        pageCourante = 0
        view?.apply {
            // when click on button more : load more data with a API call
            findViewById<Button>(R.id.more_data).setOnClickListener {
                pageCourante++
                homeSpotsViewModel.doRemoteAction(
                    pageCourante * MyAccountFragment.NBELEMENTS,
                    pageCourante * MyAccountFragment.NBELEMENTS + MyAccountFragment.NBELEMENTS,
                    ::onSpotsApiResult
                )
            }

            findViewById<RecyclerView>(R.id.rvSpots).layoutManager =
                LinearLayoutManager(requireContext())

            // clear the spots list before displayed it again
            content.clear()
            findViewById<RecyclerView>(R.id.rvSpots)?.adapter?.notifyDataSetChanged()
            // get spots
            homeSpotsViewModel.doRemoteAction(
                pageCourante * MyAccountFragment.NBELEMENTS,
                pageCourante * MyAccountFragment.NBELEMENTS + MyAccountFragment.NBELEMENTS,
                ::onSpotsApiResult
            )

            findViewById<RecyclerView>(R.id.rvSpots).adapter = HomeSpotsAdapter(content) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:" + it.position.latitude + "," + it.position.longitude)
                    )
                )
            }
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
                            this.view?.findViewById<RecyclerView>(R.id.rvSpots)?.adapter?.notifyDataSetChanged()
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.no_more_data), Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.not_received), Toast.LENGTH_SHORT).show()
                    }
                } catch (err: Exception) {
                    System.err.println("On cherche à modifier une vue qui n'est plus affiché " + err)
                }
            }
        } catch (err: Exception) {
            System.err.println("On cherche à modifier une vue qui n'est plus affiché " + err)
        }
    }
}
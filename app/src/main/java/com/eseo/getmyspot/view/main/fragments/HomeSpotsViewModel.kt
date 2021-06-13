package com.eseo.getmyspot.view.main.fragments

import androidx.lifecycle.viewModelScope
import com.eseo.getmyspot.data.models.GetSpotsResult
import com.eseo.getmyspot.domain.repository.RemoteRepository
import com.eseo.getmyspot.utils.mvvm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeSpotsViewModel(private val remoteRepository: RemoteRepository) : BaseViewModel() {

    fun doRemoteAction(range_min: Number, range_max: Number, callback:(result: GetSpotsResult?, isError:Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val spots = remoteRepository.getSpots(null, range_min, range_max)
                callback(spots, false)
            } catch (err: Exception) {
                callback(null, true)
            }
        }
    }
}
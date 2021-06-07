package com.eseo.getmyspot.view.main.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eseo.getmyspot.data.models.GetSpotsResult
import com.eseo.getmyspot.domain.repository.RemoteRepository
import com.eseo.getmyspot.utils.mvvm.BaseViewModel
import com.eseo.getmyspot.view.Failed
import com.eseo.getmyspot.view.Loading
import com.eseo.getmyspot.view.ViewModelState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAccountSpotsViewModel(private val remoteRepository: RemoteRepository) : BaseViewModel() {
    val states = MutableLiveData<ViewModelState>()

    fun doRemoteAction(pseudo: String?, range_min: Number, range_max: Number) {
        states.postValue(Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val spots = remoteRepository.getSpots(pseudo, range_min, range_max)
                states.postValue(CallResult(spots))
            } catch (err: Exception) {
                states.postValue(Failed(err))
            }
        }
    }

    data class CallResult(val result: GetSpotsResult) : ViewModelState()
}
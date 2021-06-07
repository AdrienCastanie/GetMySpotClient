package com.eseo.getmyspot.view.main.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eseo.getmyspot.data.models.AddSpotBodyParam
import com.eseo.getmyspot.data.models.SpotModel
import com.eseo.getmyspot.domain.repository.RemoteRepository
import com.eseo.getmyspot.utils.mvvm.BaseViewModel
import com.eseo.getmyspot.view.Failed
import com.eseo.getmyspot.view.Loading
import com.eseo.getmyspot.view.ViewModelState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddSpotViewModel(private val remoteRepository: RemoteRepository) : BaseViewModel() {
    val states = MutableLiveData<ViewModelState>()

    fun doRemoteAction(newSpot: SpotModel) {
        states.postValue(Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var addSpotBodyParam = AddSpotBodyParam(
                    newSpot.pseudo,
                    newSpot.battery,
                    newSpot.position.latitude.toString(),
                    newSpot.position.longitude.toString(),
                    newSpot.pressure,
                    newSpot.brightness,
                    newSpot.image_spot
                )
                val result = remoteRepository.addSpot(addSpotBodyParam)
                states.postValue(CallResult(result))
            } catch (err: Exception) {
                states.postValue(Failed(err))
            }
        }
    }

    data class CallResult(val isCorrectlyAdded: Boolean) : ViewModelState()
}
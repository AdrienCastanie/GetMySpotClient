package com.eseo.getmyspot.view.main.fragments

import androidx.lifecycle.viewModelScope
import com.eseo.getmyspot.data.models.GetProfilePictureResult
import com.eseo.getmyspot.data.models.GetSpotsResult
import com.eseo.getmyspot.domain.repository.RemoteRepository
import com.eseo.getmyspot.utils.mvvm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAccountGetProfilePictureViewModel(private val remoteRepository: RemoteRepository) : BaseViewModel() {

    fun doRemoteAction(pseudo: String, callback:(result: GetProfilePictureResult?, isError:Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profilePictureResult = remoteRepository.getProfilePicture(pseudo)
                callback(profilePictureResult, false)
            } catch (err: Exception) {
                callback(null, true)
            }
        }
    }
}
package com.eseo.getmyspot.view.account.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eseo.getmyspot.domain.repository.RemoteRepository
import com.eseo.getmyspot.utils.mvvm.BaseViewModel
import com.eseo.getmyspot.view.Failed
import com.eseo.getmyspot.view.Loading
import com.eseo.getmyspot.view.ViewModelState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(private val remoteRepository: RemoteRepository) : BaseViewModel() {
    val states = MutableLiveData<ViewModelState>()

    fun doRemoteAction(pseudo: String, password: String) {
        states.postValue(Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = remoteRepository.connectionToAccount(pseudo, password)
                states.postValue(CallResult(result))
            } catch (err: Exception) {
                states.postValue(Failed(err))
            }
        }
    }

    data class CallResult(val isConnectToAccount: Boolean) : ViewModelState()
}
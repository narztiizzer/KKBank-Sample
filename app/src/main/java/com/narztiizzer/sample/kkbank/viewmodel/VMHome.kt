package com.narztiizzer.sample.kkbank.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narztiizzer.sample.kkbank.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class VMHome(private val repository: AppRepository): ViewModel() {
    private val _loadingDialog = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()
    private val _logoutSuccess = MutableLiveData<Boolean>()

    val loadingDialog: LiveData<Boolean> = this._loadingDialog
    val errorMessage: LiveData<String> = this._errorMessage
    val logoutSuccess: LiveData<Boolean> = this._logoutSuccess

    fun logout(){
        repository.removePinCodeFromLocal()
        _logoutSuccess.postValue(true)
    }

    fun getSampleRequest() {
        viewModelScope.launch(Dispatchers.Default) {
            _loadingDialog.postValue(true)
            try {
                val response = this@VMHome.repository.requestLogin()
                println(response)
            } catch (e: Exception){
                _errorMessage.postValue(e.message)
            }
            _loadingDialog.postValue(false)
        }
    }
}
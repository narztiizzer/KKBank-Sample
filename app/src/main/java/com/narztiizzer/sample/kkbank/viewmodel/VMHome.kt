package com.narztiizzer.sample.kkbank.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narztiizzer.sample.kkbank.model.Carousel
import com.narztiizzer.sample.kkbank.repository.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class VMHome(private val repository: AppRepository): ViewModel() {
    private val _loadingDialog = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()
    @VisibleForTesting
    val _logoutSuccess = MutableLiveData<Boolean>()
    @VisibleForTesting
    val _loadCarousel = MutableLiveData<List<Carousel>>()

    val loadingDialog: LiveData<Boolean> = this._loadingDialog
    val errorMessage: LiveData<String> = this._errorMessage
    val logoutSuccess: LiveData<Boolean> = this._logoutSuccess
    val loadCarousel: LiveData<List<Carousel>> = this._loadCarousel

    fun logout(){
        repository.removePinCodeFromLocal()
        _logoutSuccess.postValue(true)
    }

    fun getCarouselItems(dispatcher: CoroutineDispatcher = Dispatchers.Default) {
        viewModelScope.launch(dispatcher) {
            _loadingDialog.postValue(true)
            try {
                val response = this@VMHome.repository.getCacousels()
                _loadCarousel.postValue(response)
            } catch (e: Exception){
                _errorMessage.postValue(e.message)
            }
            _loadingDialog.postValue(false)
        }
    }
}
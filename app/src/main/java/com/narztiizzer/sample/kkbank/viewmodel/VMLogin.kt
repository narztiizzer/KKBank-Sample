package com.narztiizzer.sample.kkbank.viewmodel

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narztiizzer.sample.kkbank.R
import com.narztiizzer.sample.kkbank.model.User
import com.narztiizzer.sample.kkbank.repository.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class VMLogin(private val repository: AppRepository): ViewModel() {
    @VisibleForTesting
    val _loginSuccess = MutableLiveData<User>()
    @VisibleForTesting
    val _errorMessage = MutableLiveData<String>()
    private val _loadingDialog = MutableLiveData<Boolean>()

    val loginSuccess: LiveData<User> = this._loginSuccess
    val errorMessage: LiveData<String> = this._errorMessage
    val loadingDialog: LiveData<Boolean> = this._loadingDialog

    fun login(dispatcher: CoroutineDispatcher = Dispatchers.Default) {
        viewModelScope.launch(dispatcher) {
            _loadingDialog.postValue(true)
            try {
                val response = this@VMLogin.repository.requestLogin()
                _loginSuccess.postValue(response)
            } catch (e: Exception){
                _errorMessage.postValue(e.message)
            }
            _loadingDialog.postValue(false)
        }
    }

    fun validateLoginData(context: Context, username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                this._errorMessage.postValue(context.getString(R.string.username_warning_message))
                false
            }
            password.isEmpty() -> {
                this._errorMessage.postValue(context.getString(R.string.password_warning_message))
                false
            }
            else -> { true }
        }
    }
}
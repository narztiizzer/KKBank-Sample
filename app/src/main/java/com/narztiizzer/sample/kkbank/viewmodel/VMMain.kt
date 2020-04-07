package com.narztiizzer.sample.kkbank.viewmodel

import androidx.lifecycle.ViewModel
import com.narztiizzer.sample.kkbank.repository.AppRepository

class VMMain(private val repository: AppRepository): ViewModel() {
    fun isHasPinCache(): Boolean = !this.repository.getPinCodeFromLocal().isNullOrEmpty()
}
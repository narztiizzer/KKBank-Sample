package com.narztiizzer.sample.kkbank.repository

import com.narztiizzer.sample.kkbank.model.Carousel
import com.narztiizzer.sample.kkbank.model.User

interface Repository {
    fun requestLogin(): User?
    fun getPinCodeFromLocal(): String?
    fun savePinCodeToLocal(pincode: String)
    fun removePinCodeFromLocal()
    fun getCacousels(): List<Carousel>?
}
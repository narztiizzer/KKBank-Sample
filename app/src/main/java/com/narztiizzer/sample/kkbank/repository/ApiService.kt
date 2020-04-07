package com.narztiizzer.sample.kkbank.repository

import com.narztiizzer.sample.kkbank.model.User
import retrofit2.Call
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(): Call<User>
}
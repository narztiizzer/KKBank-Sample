package com.narztiizzer.sample.kkbank.repository

import com.narztiizzer.sample.kkbank.model.User

open class AppRepository(private val localDatabase: LocalDatabase, private val apiService: ApiService): Repository {
    override fun requestLogin(): User? = this.apiService.login().execute().body()
    override fun getPinCodeFromLocal(): String? = this.localDatabase.getPinCode()
    override fun savePinCodeToLocal(pincode: String) { this.localDatabase.savePinCode(pincode) }
    override fun removePinCodeFromLocal() { this.localDatabase.deletePinCode() }
}
package com.narztiizzer.sample.kkbank.repository

import android.content.Context

open class LocalDatabase(private val context: Context) {
    private val SHARED_PREFERENCE_KEY = "KK-SAMPLE-APP"
    private val PIN_CACHE_KEY = "PINCODE"

    fun getPinCode() : String? {
        return this.context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE).getString(PIN_CACHE_KEY, null)
    }

    fun savePinCode(pin: String){
        this.context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE).edit().apply {
            this.putString(PIN_CACHE_KEY, pin)
        }.apply()
    }

    fun deletePinCode(){
        this.context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE).edit().apply {
            this.remove(PIN_CACHE_KEY)
        }.apply()
    }
}
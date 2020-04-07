package com.narztiizzer.sample.kkbank

import android.content.Context
import android.content.SharedPreferences
import com.narztiizzer.sample.kkbank.repository.ApiService
import com.narztiizzer.sample.kkbank.repository.AppRepository
import com.narztiizzer.sample.kkbank.repository.LocalDatabase
import com.narztiizzer.sample.kkbank.utils.TestConstant
import com.narztiizzer.sample.kkbank.viewmodel.VMMain
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

class MainUnitTest {
    private val context = Mockito.mock(Context::class.java)
    private val localDatabase = LocalDatabase(context)
    private val apiService = Mockito.mock(ApiService::class.java)
    private val repository = AppRepository(localDatabase, apiService)
    private val viewModel = VMMain(repository)

    @Test
    fun checkPINCache_Test() {
        val sharePref = Mockito.mock(SharedPreferences::class.java)
        Mockito.`when`(context.getSharedPreferences(TestConstant.SHARE_PREFERENCE_TEST_KEY, Context.MODE_PRIVATE)).thenReturn(sharePref)
        Mockito.`when`(sharePref.getString(TestConstant.PINCODE_TEST_KEY, null)).thenReturn("1234")

        assertTrue(viewModel.isHasPinCache())
    }
}

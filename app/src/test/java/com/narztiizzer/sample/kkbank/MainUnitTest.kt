package com.narztiizzer.sample.kkbank

import com.narztiizzer.sample.kkbank.repository.AppRepository
import com.narztiizzer.sample.kkbank.viewmodel.VMMain
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

class MainUnitTest {
    private val repository = Mockito.mock(AppRepository::class.java)
    private val viewModel = VMMain(repository)
    @Test
    fun checkPINCache_Test() {
        Mockito.`when`(repository.getPinCodeFromLocal()).thenReturn("1234")
        assertTrue(viewModel.isHasPinCache())
    }
}

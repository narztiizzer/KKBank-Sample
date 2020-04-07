package com.narztiizzer.sample.kkbank

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.narztiizzer.sample.kkbank.repository.AppRepository
import com.narztiizzer.sample.kkbank.viewmodel.VMPinCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class PINUnitTest {

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    private val repository = mock(AppRepository::class.java)
    private val viewModel = VMPinCode(repository)
    private val observerState: Observer<VMPinCode.PinState> = mock(Observer::class.java) as Observer<VMPinCode.PinState>
    private val observerSetupResult: Observer<Boolean> = mock(Observer::class.java) as Observer<Boolean>

    @Before
    fun prepare(){
        viewModel._currentPinCodeState.observeForever(observerState)
        viewModel._setupPinCode.observeForever(observerSetupResult)
    }

    @Test
    fun comparePIN_Test() {
        Mockito.`when`(repository.getPinCodeFromLocal()).thenReturn("1234")
        viewModel.pinArray.addAll(listOf(1, 2, 3, 4))

        assert(viewModel.comparePinFromCache())
    }

    @Test
    fun validatePINWithCache_Test() {
        Mockito.`when`(repository.getPinCodeFromLocal()).thenReturn("1234")
        viewModel.changePinState(VMPinCode.PinState.STATE_VALIDATE)
        viewModel.pressToEnter(R.id.num_1)
        viewModel.pressToEnter(R.id.num_2)
        viewModel.pressToEnter(R.id.num_3)
        viewModel.pressToEnter(R.id.num_4)

        assertEquals(true, viewModel.validatePinCode.value)
    }

    @Test
    fun enterPINStateSetup_Test() {
        viewModel.changePinState(VMPinCode.PinState.STATE_SETUP)
        viewModel.pressToEnter(R.id.num_1)
        viewModel.pressToEnter(R.id.num_2)
        viewModel.pressToEnter(R.id.num_3)
        viewModel.pressToEnter(R.id.num_4)

        assertEquals(VMPinCode.PinState.STATE_SETUP_CONFIRM, viewModel.pinCodeState.value)
    }

    @Test
    fun enterPINStateConfirm_Test() {
        viewModel.changePinState(VMPinCode.PinState.STATE_SETUP)
        viewModel.pressToEnter(R.id.num_1)
        viewModel.pressToEnter(R.id.num_2)
        viewModel.pressToEnter(R.id.num_3)
        viewModel.pressToEnter(R.id.num_4)

        viewModel.pressToConfirm(R.id.num_1)
        viewModel.pressToConfirm(R.id.num_2)
        viewModel.pressToConfirm(R.id.num_3)
        viewModel.pressToConfirm(R.id.num_4)
        viewModel.pressToConfirm(R.id.num_del)
        viewModel.pressToConfirm(R.id.num_5)

        assertEquals(true, viewModel.setupPinCode.value)
    }

    @Test
    fun validatePINStateConfirm_Test() {
        viewModel.pinArray.addAll(listOf(1, 2, 3, 4))
        viewModel.confirmPinArray.addAll(listOf(1, 2, 3, 4))

        assertTrue(viewModel.validateConfirmPinCode())
    }
}

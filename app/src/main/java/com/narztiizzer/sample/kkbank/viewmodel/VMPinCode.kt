package com.narztiizzer.sample.kkbank.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.narztiizzer.sample.kkbank.repository.Repository
import com.narztiizzer.sample.kkbank.R
import com.narztiizzer.sample.kkbank.repository.AppRepository

class VMPinCode(private val repository: AppRepository): ViewModel() {
    @VisibleForTesting
    val _currentPinCodeState = MutableLiveData<PinState>()
    @VisibleForTesting
    val _setupPinCode = MutableLiveData<Boolean>()
    @VisibleForTesting
    val _pinCodeLength = MutableLiveData<Int>()
    @VisibleForTesting
    val _validatePinCode = MutableLiveData<Boolean>()

    val pinCodeState: LiveData<PinState> = this._currentPinCodeState
    val setupPinCode: LiveData<Boolean> = this._setupPinCode
    val pinCodeLength: LiveData<Int> = this._pinCodeLength
    val validatePinCode: LiveData<Boolean> = this._validatePinCode

    @VisibleForTesting
    val pinArray = arrayListOf<Int>()
    @VisibleForTesting
    val confirmPinArray = arrayListOf<Int>()

    fun onPressPinKey(id: Int){
        when(this._currentPinCodeState.value){
            PinState.STATE_VALIDATE,
            PinState.STATE_SETUP -> { this.pressToEnter(id) }
            else -> { this.pressToConfirm(id) }
        }
    }

    fun changePinState(state: PinState) = this._currentPinCodeState.postValue(state)

    @VisibleForTesting
    fun pressToEnter(id: Int){
        if(this.confirmPinArray.size <= 4) {
            if(this.pinArray.size <= 4) {
                when (id) {
                    R.id.num_0 -> { this.pinArray.add(0) }
                    R.id.num_1 -> { this.pinArray.add(1) }
                    R.id.num_2 -> { this.pinArray.add(2) }
                    R.id.num_3 -> { this.pinArray.add(3) }
                    R.id.num_4 -> { this.pinArray.add(4) }
                    R.id.num_5 -> { this.pinArray.add(5) }
                    R.id.num_6 -> { this.pinArray.add(6) }
                    R.id.num_7 -> { this.pinArray.add(7) }
                    R.id.num_8 -> { this.pinArray.add(8) }
                    R.id.num_9 -> { this.pinArray.add(9) }
                    else -> { if (this.pinArray.size > 0) this.pinArray.removeAt(this.pinArray.size - 1) }
                }

                this._pinCodeLength.postValue(this.pinArray.size)
                if(this.pinArray.size == 4) {
                    if(this._currentPinCodeState.value == PinState.STATE_SETUP){
                        this.changePinState(PinState.STATE_SETUP_CONFIRM)
                        this._pinCodeLength.postValue(0)
                    } else {
                        if(this.comparePinFromCache()) {
                            this._validatePinCode.postValue(true)
                        } else {
                            this._validatePinCode.postValue(false)
                            this.clearPinCode()
                        }
                    }
                }
            }
        }
    }

    @VisibleForTesting
    fun pressToConfirm(id: Int){
        if(this.confirmPinArray.size <= 4) {
            when (id) {
                R.id.num_0 -> { this.confirmPinArray.add(0) }
                R.id.num_1 -> { this.confirmPinArray.add(1) }
                R.id.num_2 -> { this.confirmPinArray.add(2) }
                R.id.num_3 -> { this.confirmPinArray.add(3) }
                R.id.num_4 -> { this.confirmPinArray.add(4) }
                R.id.num_5 -> { this.confirmPinArray.add(5) }
                R.id.num_6 -> { this.confirmPinArray.add(6) }
                R.id.num_7 -> { this.confirmPinArray.add(7) }
                R.id.num_8 -> { this.confirmPinArray.add(8) }
                R.id.num_9 -> { this.confirmPinArray.add(9) }
                else -> { if (this.confirmPinArray.size > 0) this.confirmPinArray.removeAt(this.confirmPinArray.size - 1) }
            }

            this._pinCodeLength.postValue(this.confirmPinArray.size)
            if(this.confirmPinArray.size == 4) {
                if(this.validateConfirmPinCode()) {
                    val pinCode = this.pinArray.joinToString("")
                    this.saveSetupPinToLocal(pinCode)
                } else {
                    this.clearPinCode()
                    this.changePinState(PinState.STATE_SETUP)
                }
            }
        }
    }

    @VisibleForTesting
    fun validateConfirmPinCode(): Boolean{
        val pinCode = this.pinArray.joinToString("")
        val confirmPinCode = this.confirmPinArray.joinToString("")
        return pinCode == confirmPinCode
    }

    @VisibleForTesting
    fun saveSetupPinToLocal(pinCode: String) {
        this.repository.savePinCodeToLocal(pinCode)
        this._setupPinCode.postValue(true)
    }

    @VisibleForTesting
    fun comparePinFromCache(): Boolean{
        val inputPin = this.pinArray.joinToString("")
        val cachePin = this.repository.getPinCodeFromLocal()

        return inputPin == cachePin
    }

    @VisibleForTesting
    fun clearPinCode(){
        this.pinArray.clear()
        this.confirmPinArray.clear()
        this._pinCodeLength.postValue(0)
    }

    enum class PinState(state: Int) {
        STATE_SETUP(1),
        STATE_SETUP_CONFIRM(2),
        STATE_VALIDATE(3)
    }
}
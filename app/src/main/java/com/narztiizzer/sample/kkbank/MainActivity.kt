package com.narztiizzer.sample.kkbank

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.narztiizzer.sample.kkbank.viewmodel.VMMain
import com.narztiizzer.sample.kkbank.viewmodel.VMPinCode
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: VMMain by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        this.prepareStartFragment()
    }

    private fun prepareStartFragment() {
        val fragment = if(this.viewModel.isHasPinCache()) PinCodeFragment.withState(VMPinCode.PinState.STATE_VALIDATE) else LoginFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}

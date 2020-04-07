package com.narztiizzer.sample.kkbank

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.narztiizzer.sample.kkbank.viewmodel.VMPinCode
import kotlinx.android.synthetic.main.pin_code_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinCodeFragment: Fragment(), View.OnClickListener {
    private val viewModel: VMPinCode by viewModel()

    companion object {
        const val STATE = "STATE"
        fun withState(state: VMPinCode.PinState): PinCodeFragment {
            return PinCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(STATE, state.name)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pin_code_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.initialValue()
        this.initialAction()
        this.initialObserver()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.num_0,
            R.id.num_1,
            R.id.num_2,
            R.id.num_3,
            R.id.num_4,
            R.id.num_5,
            R.id.num_6,
            R.id.num_7,
            R.id.num_8,
            R.id.num_9,
            R.id.num_del -> {
                this.viewModel.onPressPinKey(v.id)
            }
            else -> {}
        }
    }

    private fun initialValue(){
        val state = arguments?.getString(STATE, VMPinCode.PinState.STATE_SETUP.name)
            ?: VMPinCode.PinState.STATE_SETUP.name
        this.viewModel.changePinState(VMPinCode.PinState.valueOf(state))
    }

    private fun initialAction(){
        num_0.setOnClickListener(this)
        num_1.setOnClickListener(this)
        num_2.setOnClickListener(this)
        num_3.setOnClickListener(this)
        num_4.setOnClickListener(this)
        num_5.setOnClickListener(this)
        num_6.setOnClickListener(this)
        num_7.setOnClickListener(this)
        num_8.setOnClickListener(this)
        num_9.setOnClickListener(this)
        num_del.setOnClickListener(this)
    }

    private fun initialObserver(){
        this.viewModel.pinCodeLength.observe(this.viewLifecycleOwner, Observer {
            this.setPinCodeDot(
                pinFirstSelect = it >= 1,
                pinSecondSelect = it >= 2,
                pinThirdSelect = it >= 3,
                pinFourthSelect = it >= 4
            )
        })

        this.viewModel.pinCodeState.observe(this.viewLifecycleOwner, Observer { state ->
            pin_title.text  = when(state){
                VMPinCode.PinState.STATE_SETUP -> { this.context?.getString(R.string.setup_pin) }
                VMPinCode.PinState.STATE_SETUP_CONFIRM -> { this.context?.getString(R.string.confirm_pin) }
                else -> { this.context?.getString(R.string.validate_pin) }
            }
        })

        //Compare setup pin with confirm pin and save
        this.viewModel.setupPinCode.observe(this.viewLifecycleOwner, Observer { isSuccess ->
            this.openHomepage()
        })

        //Compare input pin with cache
        this.viewModel.validatePinCode.observe(this.viewLifecycleOwner, Observer { isPass ->
            this.openHomepage()
        })
    }

    private fun setPinCodeDot(pinFirstSelect: Boolean, pinSecondSelect: Boolean, pinThirdSelect: Boolean, pinFourthSelect: Boolean){
        pin_1.isChecked = pinFirstSelect
        pin_2.isChecked = pinSecondSelect
        pin_3.isChecked = pinThirdSelect
        pin_4.isChecked = pinFourthSelect
    }

    private fun openHomepage() {
        val context = this.requireActivity()
        context.startActivity(Intent(context, HomeActivity::class.java))
        context.finish()
    }
}
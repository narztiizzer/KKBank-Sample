package com.narztiizzer.sample.kkbank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.narztiizzer.sample.kkbank.viewmodel.VMLogin
import com.narztiizzer.sample.kkbank.viewmodel.VMPinCode
import kotlinx.android.synthetic.main.login_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment: Fragment() {
    private val viewModel: VMLogin by viewModel()
    private val loadingDialogFragment: LoadingDialogFragment by lazy { LoadingDialogFragment() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.initialAction()
        this.initialObserver()
    }

    private fun initialAction() {
        login.setOnClickListener {
            if(viewModel.validateLoginData(
                    this.requireContext(),
                    username.text.toString(),
                    password.text.toString())
            ) {
                this.viewModel.login()
            }
        }
    }

    private fun initialObserver() {
        this.viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(container, it, Snackbar.LENGTH_SHORT).apply {
                setAction("OK"){ this.dismiss() }
            }.show()
        })

        this.viewModel.loginSuccess.observe(viewLifecycleOwner, Observer {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, PinCodeFragment.withState(VMPinCode.PinState.STATE_SETUP))
                ?.commit()
        })

        this.viewModel.loadingDialog.observe(viewLifecycleOwner, Observer { isShow ->
            if(isShow) loadingDialogFragment.show(childFragmentManager, "LOADING")
            else loadingDialogFragment.dismissAllowingStateLoss()
        })
    }
}
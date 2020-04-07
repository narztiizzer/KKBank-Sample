package com.narztiizzer.sample.kkbank

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.narztiizzer.sample.kkbank.model.User
import com.narztiizzer.sample.kkbank.repository.AppRepository
import com.narztiizzer.sample.kkbank.utils.CoroutinesTestRule
import com.narztiizzer.sample.kkbank.viewmodel.VMLogin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class LoginUnitTest {
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesTestRule()

    private val repository = mock(AppRepository::class.java)
    private val viewModel = VMLogin(repository)
    private val observerLogin: Observer<User> = mock(Observer::class.java) as Observer<User>

    @Before
    fun prepare(){
        viewModel._loginSuccess.observeForever(observerLogin)
    }

    @Test
    fun validateInput_Test() {
        val context = mock(Context::class.java)
        val username = "nattapongp"
        val password = "1234"

        Mockito.`when`(context.getString(R.string.username_warming_message)).thenReturn("")
        Mockito.`when`(context.getString(R.string.password_warming_message)).thenReturn("")
        assertTrue(viewModel.validateLoginData(context, username, password))
    }

    @Test
    fun validateLoginResult_Test() {
        Mockito.`when`(repository.requestLogin()).thenReturn(
            User(
                "Nattapong",
                "Poomtong",
                "nattapong.poom@gmail.com",
                "0870328735"
            )
        )
        viewModel.login()
        assertEquals("Nattapong", viewModel._loginSuccess.value?.firstName)
    }
}

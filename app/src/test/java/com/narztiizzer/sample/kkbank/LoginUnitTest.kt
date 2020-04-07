package com.narztiizzer.sample.kkbank

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.narztiizzer.sample.kkbank.model.User
import com.narztiizzer.sample.kkbank.repository.ApiService
import com.narztiizzer.sample.kkbank.repository.AppRepository
import com.narztiizzer.sample.kkbank.repository.LocalDatabase
import com.narztiizzer.sample.kkbank.utils.CoroutinesTestRule
import com.narztiizzer.sample.kkbank.viewmodel.VMLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Response

class LoginUnitTest {
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesTestRule()

    private val context = mock(Context::class.java)
    private val localDatabase = LocalDatabase(context)
    private val apiService = mock(ApiService::class.java)
    private val repository = AppRepository(localDatabase, apiService)
    private val viewModel = VMLogin(repository)
    private val observerLogin: Observer<User> = mock(Observer::class.java) as Observer<User>

    @Before
    fun prepare(){
        viewModel._loginSuccess.observeForever(observerLogin)
    }

    @Test
    fun validateInput_Test() {
        val username = "nattapongp"
        val password = "1234"

        Mockito.`when`(context.getString(R.string.username_warning_message)).thenReturn("")
        Mockito.`when`(context.getString(R.string.password_warning_message)).thenReturn("")
        assertTrue(viewModel.validateLoginData(context, username, password))
    }

    @Test
    fun validateLoginResult_Test() {
        val mockResponse: Call<User> = mock(Call::class.java) as Call<User>
        Mockito.`when`(apiService.login()).thenReturn(mockResponse)
        Mockito.`when`(mockResponse.execute()).thenReturn(
            Response.success(User(
                "Nattapong",
                "Poomtong",
                "nattapong.poom@gmail.com",
                "0870328735"
            ))
        )

        viewModel.login(Dispatchers.Unconfined)
        assertEquals("Nattapong", viewModel._loginSuccess.value?.firstName)
    }
}

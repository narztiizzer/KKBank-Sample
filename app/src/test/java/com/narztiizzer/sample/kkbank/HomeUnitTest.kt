package com.narztiizzer.sample.kkbank

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.narztiizzer.sample.kkbank.model.Carousel
import com.narztiizzer.sample.kkbank.model.User
import com.narztiizzer.sample.kkbank.repository.ApiService
import com.narztiizzer.sample.kkbank.repository.AppRepository
import com.narztiizzer.sample.kkbank.repository.LocalDatabase
import com.narztiizzer.sample.kkbank.utils.CoroutinesTestRule
import com.narztiizzer.sample.kkbank.viewmodel.VMHome
import com.narztiizzer.sample.kkbank.viewmodel.VMLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Response

class HomeUnitTest {
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
    private val viewModel = VMHome(repository)
    private val observerCarousel: Observer<List<Carousel>> = mock(Observer::class.java) as Observer<List<Carousel>>

    @Before
    fun prepare(){
        viewModel._loadCarousel.observeForever(observerCarousel)
    }

    @Test
    fun logout_Test() {
        val sharePref = mock(SharedPreferences::class.java)
        Mockito.`when`(context.getSharedPreferences("KK-SAMPLE-APP", Context.MODE_PRIVATE)).thenReturn(sharePref)
        Mockito.`when`(sharePref.getString("PINCODE", null)).thenReturn("1234")

        assertEquals("1234", repository.getPinCodeFromLocal())
    }

    @Test
    fun validateLoginResult_Test() {
        val mockResponse: Call<List<Carousel>> = mock(Call::class.java) as Call<List<Carousel>>
        Mockito.`when`(apiService.carousels()).thenReturn(mockResponse)
        Mockito.`when`(mockResponse.execute()).thenReturn(
            Response.success(
                arrayListOf(Carousel(
                    1,
                    "http://google.co.th"
                ))
            )
        )

        viewModel.getCarouselItems(Dispatchers.Unconfined)
        assertEquals(1, viewModel.loadCarousel.value?.size)
    }
}

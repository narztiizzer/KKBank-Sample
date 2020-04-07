package com.narztiizzer.sample.kkbank.base

import android.app.Application
import com.narztiizzer.sample.kkbank.repository.ApiService
import com.narztiizzer.sample.kkbank.repository.AppRepository
import com.narztiizzer.sample.kkbank.repository.LocalDatabase
import com.narztiizzer.sample.kkbank.viewmodel.VMHome
import com.narztiizzer.sample.kkbank.viewmodel.VMLogin
import com.narztiizzer.sample.kkbank.viewmodel.VMMain
import com.narztiizzer.sample.kkbank.viewmodel.VMPinCode
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        this.initialKoin()
    }

    private fun initialKoin(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://5e4ca8139b6805001438ec44.mockapi.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val mainModule = module {
            single { retrofit.create(ApiService::class.java) }
            single { LocalDatabase(get()) }
            single { AppRepository(get(), get()) }
            viewModel { VMMain(get()) }
        }
        val loginModule = module {
            viewModel { VMLogin(get()) }
        }
        val pinModule = module {
            viewModel { VMPinCode(get()) }
        }
        val homeModule = module {
            viewModel { VMHome(get()) }
        }

        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(mainModule, loginModule, pinModule, homeModule))
        }
    }
}
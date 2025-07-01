package com.example.ridemanagementhmi

import android.app.Application
import com.example.ridemanagementhmi.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.mappls.sdk.maps.Mappls
import com.mappls.sdk.services.account.MapplsAccountManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RideManagementHMIApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Mappls SDK with only Context

                MapplsAccountManager.getInstance().apply {
                    this.restAPIKey = "b9930f54f59b5d54d4ebe585ec5ef39f"
                    this.mapSDKKey = "b9930f54f59b5d54d4ebe585ec5ef39f"
                    this.atlasClientId =
                        "96dHZVzsAushDz-dq9c8PtU3UGlAoyi8ftcst7rKLiU7wNQPtTFS6_fDgNwZfDISR2H4u7XdIrZP2lAu4mrM3A=="
                    this.atlasClientSecret =
                        "lrFxI-iSEg-uxRfOiSaiIGFV_fc1b_2Vf43XCJhDm-1XqmyD5HHL7ahMo9UdjUJ-MVXDTMvv2zo7Iuc4m_NlJJu9LWsOR9BO"
                }
        Mappls.getInstance(this)


        startKoin {
            androidContext(this@RideManagementHMIApp)
            modules(appModule)
        }
    }
} 
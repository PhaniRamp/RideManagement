package com.example.ridemanagementhmi.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.ridemanagementhmi.repository.RideRepository
import com.example.ridemanagementhmi.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext

val appModule = module {
    single { RideRepository() }
    viewModel { MainViewModel(androidContext() as android.app.Application, get()) }
} 
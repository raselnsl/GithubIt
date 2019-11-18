package com.chetdeva.githubit

import android.app.Application
import com.chetdeva.githubit.api.StationLeaveApi
import com.chetdeva.githubit.api.StationLeaveApiService
import com.chetdeva.githubit.data.GithubRepository
import com.chetdeva.githubit.ui.home.HomeViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.util.concurrent.Executors

const val CHANNEL_ID_RECEIVED_LEAVE_REQUEST = "ID_RECEIVED_LEAVE_REQUEST"

class MVVMApplication : Application(), KodeinAware {

    init {
       // createNotificationChannel()
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        bind() from singleton { StationLeaveApi.create() }
        bind() from singleton { StationLeaveApiService(instance()) }
        bind() from singleton { Executors.newFixedThreadPool(5) }

        bind() from singleton { GithubRepository(instance(), instance()) }

        bind() from provider { HomeViewModelFactory(instance()) }
    }
}
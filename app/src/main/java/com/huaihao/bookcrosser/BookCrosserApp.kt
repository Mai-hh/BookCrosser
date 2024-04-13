package com.huaihao.bookcrosser

import android.app.Application
import com.huaihao.bookcrosser.network.BookCrosserApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class BookCrosserApp : Application() {
    override fun onCreate() {
        super.onCreate()

        BookCrosserApi.init(this@BookCrosserApp)

        startKoin {
            androidContext(this@BookCrosserApp)
            modules(appModule)
        }
    }
}
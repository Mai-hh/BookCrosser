package com.huaihao.bookcrosser.initializer

import android.content.Context
import androidx.startup.Initializer
import com.huaihao.bookcrosser.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinInjectionInitializer : Initializer<Unit> {
    override fun create(context: Context) {

    }


    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(NetworkApiInitializer::class.java)
    }
}
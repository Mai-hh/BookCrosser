package com.huaihao.bookcrosser.initializer

import android.content.Context
import androidx.startup.Initializer
import com.huaihao.bookcrosser.network.BookCrosserApi

class NetworkApiInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        BookCrosserApi.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
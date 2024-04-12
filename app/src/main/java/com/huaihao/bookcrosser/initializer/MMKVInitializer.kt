package com.huaihao.bookcrosser.initializer

import android.content.Context
import androidx.startup.Initializer
import com.huaihao.bookcrosser.util.MMKVUtil

class MMKVInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        MMKVUtil.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
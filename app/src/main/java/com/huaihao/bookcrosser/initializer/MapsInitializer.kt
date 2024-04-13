package com.huaihao.bookcrosser.initializer

import android.content.Context
import androidx.startup.Initializer
import com.amap.api.maps.MapsInitializer

class MapsInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        MapsInitializer.updatePrivacyShow(context, true, true)
        MapsInitializer.updatePrivacyAgree(context, true)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
package com.huaihao.bookcrosser.model

import com.google.android.gms.maps.model.LatLng

data class Drifting(
    val book: Book,
    val location: LatLng,
    val sender: User
)
package com.huaihao.bookcrosser.model

import com.google.android.gms.maps.model.LatLng

data class BookMarker(
    val book: Book,
    val position: LatLng
)

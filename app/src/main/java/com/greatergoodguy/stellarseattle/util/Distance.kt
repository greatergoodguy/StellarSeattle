package com.greatergoodguy.stellarseattle.util

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt

fun distance(lat1: Float, long1: Float, lat2: Float, long2: Float): Float {
    var p = 0.017453292519943295;    // 2 * Math.PI / 360
    var a = 0.5 - cos((lat2 - lat1) * p) /2 +
            cos(lat1 * p) * cos(lat2 * p) *
            (1 - cos((long2 - long1) * p))/2;

    return (12742 * asin(sqrt(a))).toFloat(); // 2 * R; R = 6371 km
}

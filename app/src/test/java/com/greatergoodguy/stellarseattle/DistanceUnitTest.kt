package com.greatergoodguy.stellarseattle

import com.greatergoodguy.stellarseattle.util.distance
import org.junit.Test

import org.junit.Assert.*
import kotlin.math.abs

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DistanceUnitTest {
    @Test
    fun distance_isCorrect() {
        val lat1 = 47.6062095f
        val long1 = -122.3320708f
        val lat2 = 47.60475923205166f
        val long2 = -122.33636210125788f
        val expectedValue = 0.3601f
        val actualValue = distance(lat1, long1, lat2, long2)

        assertTrue(abs(expectedValue - actualValue) < 0.001f)
    }
}

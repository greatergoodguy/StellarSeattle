package com.greatergoodguy.stellarseattle.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


private object Key {
    const val STELLAR_SEATTLE_SHARED_PREFS = "STELLAR_SEATTLE_SHARED_PREFS"
    const val FAVORITE_VENUE_IDS = "FAVORITE_VENUE_IDS"
}

private fun getFavoriteVenueIds(context: Context): MutableSet<String> {
    val prefs: SharedPreferences = context.getSharedPreferences(Key.STELLAR_SEATTLE_SHARED_PREFS, MODE_PRIVATE)
    return prefs.getStringSet(Key.FAVORITE_VENUE_IDS, mutableSetOf()) ?: mutableSetOf()
}

fun isFavoriteVenue(context: Context, venueId: String):Boolean {
    val venueIds = getFavoriteVenueIds(context)
    return venueIds.contains(venueId)
}

fun addFavoriteVenue(context: Context, venueId: String) {
    val venueIds = getFavoriteVenueIds(context)
    venueIds.add(venueId)

    val editor: SharedPreferences.Editor = context.getSharedPreferences(Key.STELLAR_SEATTLE_SHARED_PREFS, MODE_PRIVATE).edit()
    editor.putStringSet(Key.FAVORITE_VENUE_IDS, venueIds)
    editor.apply()
}

fun removeFavoriteVenue(context: Context, venueId: String) {
    val venueIds = getFavoriteVenueIds(context)
    venueIds.remove(venueId)

    val editor: SharedPreferences.Editor = context.getSharedPreferences(Key.STELLAR_SEATTLE_SHARED_PREFS, MODE_PRIVATE).edit()
    editor.putStringSet(Key.FAVORITE_VENUE_IDS, venueIds)
    editor.apply()
}

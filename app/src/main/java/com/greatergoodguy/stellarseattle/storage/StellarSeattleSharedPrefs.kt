package com.greatergoodguy.stellarseattle.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


private object Key {
    const val STELLAR_SEATTLE_SHARED_PREFS = "STELLAR_SEATTLE_SHARED_PREFS"
    const val FAVORITE_VENUE_IDS = "FAVORITE_VENUE_IDS"
}

/*
    According to the documentation for prefs.getStringSet:

    Note that you must not modify the set instance returned by this call.
    The consistency of the stored data is not guaranteed if you do,
    nor is your ability to modify the instance at all.

    This is why I am transforming this set to another MutableSet

    https://stackoverflow.com/questions/10720028/android-sharedpreferences-not-saving
 */
fun getFavoriteVenueIds(context: Context): MutableSet<String> {
    val prefs: SharedPreferences = context.getSharedPreferences(Key.STELLAR_SEATTLE_SHARED_PREFS, MODE_PRIVATE)
    return (prefs.getStringSet(Key.FAVORITE_VENUE_IDS, mutableSetOf()) ?: mutableSetOf()).toMutableSet()
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

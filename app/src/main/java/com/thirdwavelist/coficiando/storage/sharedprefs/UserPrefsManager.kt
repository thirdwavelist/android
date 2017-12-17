package com.thirdwavelist.coficiando.storage.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.thirdwavelist.coficiando.di.qualifiers.AppContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefsManager @Inject constructor(@AppContext context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE)

    var isFilteringEnabled: Boolean
        get() = preferences[FILTER_ENABLED]
        set(value) {
            preferences[FILTER_ENABLED] = value
        }

    companion object {
        private const val SHARED_PREFS_FILENAME = "userPrefs"

        private const val FILTER_ENABLED = "FILTER_ENABLED"
    }
}
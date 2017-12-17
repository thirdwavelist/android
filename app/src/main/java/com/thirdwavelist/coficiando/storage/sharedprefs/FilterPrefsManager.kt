package com.thirdwavelist.coficiando.storage.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.thirdwavelist.coficiando.di.qualifiers.AppContext
import com.thirdwavelist.coficiando.storage.db.cafe.BeanOriginType
import com.thirdwavelist.coficiando.storage.db.cafe.BeanRoastType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterPrefsManager @Inject constructor(@AppContext context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)

    var isInterestedInBrewMethodEspresso: Boolean
        get() = preferences[INTERESTED_IN_BREW_METHOD_ESPRESSO]
        set(value) {
            preferences[INTERESTED_IN_BREW_METHOD_ESPRESSO] = value
        }

    var isInterestedInBrewMethodAeropress: Boolean
        get() = preferences[INTERESTED_IN_BREW_METHOD_AEROPRESS]
        set(value) {
            preferences[INTERESTED_IN_BREW_METHOD_AEROPRESS] = value
        }

    var isInterestedInBrewMethodPourOver: Boolean
        get() = preferences[INTERESTED_IN_BREW_METHOD_POUR_OVER]
        set (value) {
            preferences[INTERESTED_IN_BREW_METHOD_POUR_OVER] = value
        }

    var isInterestedInBrewMethodColdBrew: Boolean
        get() = preferences[INTERESTED_IN_BREW_METHOD_COLD_BREW]
        set(value) {
            preferences[INTERESTED_IN_BREW_METHOD_COLD_BREW] = value
        }

    var isInterestedInBrewMethodSyphon: Boolean
        get() = preferences[INTERESTED_IN_BREW_METHOD_SYPHON]
        set(value) {
            preferences[INTERESTED_IN_BREW_METHOD_SYPHON] = value
        }

    var isInterestedInBrewMethodFullImmersive: Boolean
        get() = preferences[INTERESTED_IN_BREW_METHOD_FULL_IMMERSION]
        set(value) {
            preferences[INTERESTED_IN_BREW_METHOD_FULL_IMMERSION] = value
        }

    var beanOriginType: BeanOriginType
        get() = preferences[INTERESTED_IN_BEAN_ORIGIN]
        set(value) {
            preferences[INTERESTED_IN_BEAN_ORIGIN] = value
        }

    var beanRoastType: BeanRoastType
        get() = preferences[INTERESTED_IN_BEAN_ROAST]
        set(value) {
            preferences[INTERESTED_IN_BEAN_ROAST] = value
        }

    companion object {
        private const val SHARED_PREFS_FILENAME = "filterPrefs"

        private const val INTERESTED_IN_BREW_METHOD_ESPRESSO = "FILTER_BREW_METHOD_ESPRESSO"
        private const val INTERESTED_IN_BREW_METHOD_AEROPRESS = "FILTER_BREW_METHOD_AEROPRESS"
        private const val INTERESTED_IN_BREW_METHOD_SYPHON = "FILTER_BREW_METHOD_SYPHON"
        private const val INTERESTED_IN_BREW_METHOD_COLD_BREW = "FILTER_BREW_METHOD_COLD_BREW"
        private const val INTERESTED_IN_BREW_METHOD_POUR_OVER = "FILTER_BREW_METHOD_POUR_OVER"
        private const val INTERESTED_IN_BREW_METHOD_FULL_IMMERSION = "FILTER_BREW_METHOD_FULL_IMMERSION"

        private const val INTERESTED_IN_BEAN_ORIGIN = "FILTER_BEAN_ORIGIN_SINGLE"
        private const val INTERESTED_IN_BEAN_ROAST = "FILTER_BEAN_ROAST_DARK"
    }
}
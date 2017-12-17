package com.thirdwavelist.coficiando.storage.sharedprefs

import android.content.SharedPreferences
import com.thirdwavelist.coficiando.storage.db.cafe.BeanOriginType
import com.thirdwavelist.coficiando.storage.db.cafe.BeanRoastType

internal operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is Boolean -> edit { it.putBoolean(key, value) }
        is BeanRoastType -> edit { it.putInt(key, value.ordinal) }
        is BeanOriginType -> edit { it.putInt(key, value.ordinal) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

internal inline operator fun <reified T : Any> SharedPreferences.get(key: String): T =
    when (T::class) {
        Boolean::class -> getBoolean(key, true) as T
        BeanRoastType::class -> BeanRoastType.values()[getInt(key, 0)] as T
        BeanOriginType::class -> BeanOriginType.values()[getInt(key, 0)] as T
        else -> throw UnsupportedOperationException("Not yet implemented")
    }

internal inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2017, Antal János Monori
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
        String::class -> getString(key, null) as T
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

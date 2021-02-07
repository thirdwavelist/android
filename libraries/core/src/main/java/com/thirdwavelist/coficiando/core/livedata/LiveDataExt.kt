package com.thirdwavelist.coficiando.core.livedata

import androidx.lifecycle.LiveData

fun <T> LiveData<T>.test() = mutableListOf<T>().also { list ->
    observeForever { list.add(it) }
}
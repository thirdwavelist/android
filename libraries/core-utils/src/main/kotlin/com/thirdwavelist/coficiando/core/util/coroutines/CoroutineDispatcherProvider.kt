package com.thirdwavelist.coficiando.core.util.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * An abstraction to remove platform specific Threading implementation where context is used,
 * and also adding the possibility to override the pre-defined threads
 */
interface CoroutineDispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}
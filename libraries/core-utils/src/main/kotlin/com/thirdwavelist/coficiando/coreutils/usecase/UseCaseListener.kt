package com.thirdwavelist.coficiando.coreutils.usecase

import kotlinx.coroutines.CancellationException

/**
 * TODO
 */
class UseCaseListener<T> {

    private var onLoadingChange: ((Boolean) -> Unit)? = null
    private var onComplete: ((T) -> Unit)? = null
    private var onError: ((Throwable) -> Unit)? = null
    private var onCancel: ((CancellationException) -> Unit)? = null

    /**
     * TODO
     */
    fun onLoadingChange(function: (Boolean) -> Unit) {
        onLoadingChange = function
    }

    /**
     * TODO
     */
    fun onComplete(function: (T) -> Unit) {
        onComplete = function
    }

    /**
     * TODO
     */
    fun onError(function: (Throwable) -> Unit) {
        onError = function
    }

    /**
     * TODO
     */
    fun onCancel(function: (CancellationException) -> Unit) {
        onCancel = function
    }

    /**
     * TODO
     */
    operator fun invoke(isLoading: Boolean) = onLoadingChange?.invoke(isLoading)

    /**
     * TODO
     */
    operator fun invoke(result: T) = onComplete?.invoke(result)

    /**
     * TODO
     */
    operator fun invoke(error: Throwable) = onError?.invoke(error)

    /**
     * TODO
     */
    operator fun invoke(error: CancellationException) = onCancel?.invoke(error)
}
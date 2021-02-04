package com.thirdwavelist.coficiando.core

import kotlinx.coroutines.CancellationException

class UseCaseListener<T> {

    private var onLoadingChange: ((Boolean) -> Unit)? = null
    private var onComplete: ((T) -> Unit)? = null
    private var onError: ((Throwable) -> Unit)? = null
    private var onCancel: ((CancellationException) -> Unit)? = null

    fun onLoadingChange(function: (Boolean) -> Unit) {
        onLoadingChange = function
    }

    fun onComplete(function: (T) -> Unit) {
        onComplete = function
    }

    fun onError(function: (Throwable) -> Unit) {
        onError = function
    }

    fun onCancel(function: (CancellationException) -> Unit) {
        onCancel = function
    }

    operator fun invoke(isLoading: Boolean) = onLoadingChange?.invoke(isLoading)

    operator fun invoke(result: T) = onComplete?.invoke(result)

    operator fun invoke(error: Throwable) = onError?.invoke(error)

    operator fun invoke(error: CancellationException) = onCancel?.invoke(error)
}
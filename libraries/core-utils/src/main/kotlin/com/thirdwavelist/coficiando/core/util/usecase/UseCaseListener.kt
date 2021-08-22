package com.thirdwavelist.coficiando.core.util.usecase

import kotlinx.coroutines.CancellationException

/**
 * A listener class implementing the Service Callback pattern used in the asynchronous communication
 * with the [UseCase] implementation. Returns a [T] type provided by the [UseCase]
 *
 * ```
 * UseCase<T>.withParams(Params).execute {
 *     onComplete {
 *         handleResult(it)
 *     }
 *     onError {
 *         handleError(it)
 *     }
 *     onCancel {
 *         handleCancellation(it)
 *     }
 *     onLoadingChange {
 *         handleLoading(it)
 *     }
 * }
 * ```
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class UseCaseListener<T> {

    private var onLoadingChange: ((Boolean) -> Unit)? = null
    private var onComplete: ((T) -> Unit)? = null
    private var onError: ((Throwable) -> Unit)? = null
    private var onCancel: ((CancellationException) -> Unit)? = null

    /**
     * Called when [UseCase.execute] has started the Job, representing a loading [Boolean] value.
     * Immediately receives a callback with true, and once either [UseCaseListener.onComplete],
     * [UseCaseListener.onError] or [UseCaseListener.onCancel] is called, returns with false in the end.
     */
    fun onLoadingChange(function: (Boolean) -> Unit) {
        onLoadingChange = function
    }

    /**
     * Called when [UseCase.execute] Job has finished successfully and [T] entity is returned.
     */
    fun onComplete(function: (T) -> Unit) {
        onComplete = function
    }

    /**
     * Called when [UseCase.execute] Job has failed which is represented in [Throwable].
     */
    fun onError(function: (Throwable) -> Unit) {
        onError = function
    }

    /**
     * Called when [UseCase.execute] Job within the CoroutineContext created is cancelled. More
     * info provided in the [CancellationException]
     */
    fun onCancel(function: (CancellationException) -> Unit) {
        onCancel = function
    }

    operator fun invoke(isLoading: Boolean) = onLoadingChange?.invoke(isLoading)
    operator fun invoke(result: T) = onComplete?.invoke(result)
    operator fun invoke(error: Throwable) = onError?.invoke(error)
    operator fun invoke(error: CancellationException) = onCancel?.invoke(error)
}
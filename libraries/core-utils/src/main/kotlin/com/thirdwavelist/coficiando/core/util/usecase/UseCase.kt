package com.thirdwavelist.coficiando.core.util.usecase

import com.thirdwavelist.coficiando.core.util.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Abstraction inspired by CLEAN Architecture to represent business actions by orchestrating the
 * required data flow to and from entities
 *
 * Defined by it's [T] entity type that the business logic is constructed around, and [Params]
 * that is of type [Any] and defines the input required for the use case implementation
 *
 * @param dispatcherProvider [CoroutineDispatcherProvider] abstraction for threading
 */
abstract class UseCase<T, Params>(dispatcherProvider: CoroutineDispatcherProvider) where Params : Any {

    private val backgroundContext: CoroutineContext = dispatcherProvider.io
    private val foregroundContext: CoroutineContext = dispatcherProvider.main

    private lateinit var job: Job
    private lateinit var params: Params

    /**
     * Function to set the parameters required for the [UseCase] and uses method chaining.
     *
     * Note: This method must be called prior to [UseCase.execute] is called!
     */
    fun withParams(params: Params) = this.also { it.params = params }


    /**
     * Main entry-point into the execution of the [UseCase]
     *
     * @param useCaseListener [UseCaseListener] lambda for callback providing the [T] entity
     */
    fun execute(useCaseListener: UseCaseListener<T>.() -> Unit) {
        val listener = UseCaseListener<T>().apply { useCaseListener() }
        cancel()
        job = Job()
        CoroutineScope(foregroundContext + job).launch {
            listener(true)
            try {
                val result = withContext(backgroundContext) {
                    executeOnBackground()
                }
                listener(result)
            } catch (cancellationException: CancellationException) {
                listener(cancellationException)
            } catch (e: Exception) {
                listener(e)
            } finally {
                listener(false)
            }
        }
    }

    private fun cancel() {
        if (this::job.isInitialized) {
            job.apply {
                cancelChildren()
                cancel()
            }
        }
    }

    protected abstract suspend fun executeOnBackground(): T

    protected fun getParams(): Params {
        return if (this::params.isInitialized) params else {
            throw RuntimeException("You have to initialize the required parameters before execute.")
        }
    }

    protected suspend fun <T> runAsync(
            context: CoroutineContext = backgroundContext, block: suspend () -> T): Deferred<T> {
        return CoroutineScope(context + job).async { block.invoke() }
    }
}

/**
 * Simple construct representing empty params used in conjunction with [UseCase]
 */
object None
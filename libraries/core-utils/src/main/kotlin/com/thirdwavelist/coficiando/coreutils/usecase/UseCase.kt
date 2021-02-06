package com.thirdwavelist.coficiando.coreutils.usecase

import com.thirdwavelist.coficiando.coreutils.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * TODO
 */
abstract class UseCase<T, Params>(dispatcherProvider: CoroutineDispatcherProvider) where Params : Any {

    private val backgroundContext: CoroutineContext = dispatcherProvider.io
    private val foregroundContext: CoroutineContext = dispatcherProvider.main

    private lateinit var job: Job
    private lateinit var params: Params

    /**
     * TODO
     */
    fun withParams(params: Params) = this.also { it.params = params }


    /**
     * TODO
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
 * TODO
 */
object None
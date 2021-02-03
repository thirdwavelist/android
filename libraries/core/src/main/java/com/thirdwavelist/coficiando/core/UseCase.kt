package com.thirdwavelist.coficiando.core

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class UseCase<T, Params> where Params : Any {

    private val backgroundContext: CoroutineContext = Dispatchers.IO
    private val foregroundContext: CoroutineContext = Dispatchers.Main

    private lateinit var job: Job
    private lateinit var params: Params

    /**
     *
     */
    fun withParams(params: Params) = this.also { it.params = params }


    /**
     *
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

object None
package com.example.foundation.model.tasks

import com.example.foundation.model.ErrorResult
import com.example.foundation.model.FinalResult
import com.example.foundation.model.SuccessResult
import com.example.foundation.model.tasks.dispatchers.Dispatcher
import com.example.foundation.model.tasks.dispatchers.ImmediateDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


typealias TaskListener<T> = (FinalResult<T>) -> Unit

class CancelledException(
    originalException: Exception? = null
) : Exception(originalException)

/**
 * Base interface for all async operation
 *
 */

interface Task<T> {


    /**
     * Blocking method for waiting and getting result.
     * Throws exception in case of error.
     * Task may be executed only once.
     * @throws [IllegalStateException] if task has been already executed
     * @throws [CancelledException] if task has been cancelled
     */


    fun await(): T

    /**
     * Non-blocking method for listening task results.
     * If task canceled before finishing, listening is not called.
     * If task is canceled before calling method, task is not executed.
     * Task may be executed only once.
     *
     * Listeners is called in main thread
     * Listeners is called via the specified dispatcher. Usually it is [MainThreadDispather]
     *  @throws [IllegalStateException] if task has been already executed.
     */


    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    /**
     * Cancel this task and remove listener assigned by [enqueue]
     */

    fun cancel()


    suspend fun suspend(): T = suspendCancellableCoroutine { continuation ->
        enqueue(ImmediateDispatcher()){
            continuation.invokeOnCancellation { cancel() }
           when(it) {
               is SuccessResult -> continuation.resume(it.data)
               is ErrorResult -> continuation.resumeWithException(it.exception)
           }
        }
    }
}
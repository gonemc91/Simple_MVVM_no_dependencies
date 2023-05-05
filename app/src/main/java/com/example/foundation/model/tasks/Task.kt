package com.example.foundation.model.tasks

import com.example.foundation.model.FinalResult
import com.example.foundation.model.tasks.dispatchers.Dispatcher
import java.lang.Exception


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

    fun enqueue(listener: TaskListener<T>)
    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    /**
     * Cancel this task and remove listener assigned by [enqueue]
     */

    fun cancel()


}
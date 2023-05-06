package com.example.foundation.model.tasks

import com.example.foundation.model.tasks.dispatchers.Dispatcher
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Wrapper class for other task.
 * Contains common synchronized logic. Ensures this method of the wrapper task
 * are executed in correct order. Doesn't  allow corner cases: e.g. launching task more than 1 time,
 * triggered  listeners more than 1 time, launching task after cancelling, cancelling already finished
 * task and so on.
 *
 */


class SynchronizedTask<T>(
    private val task: Task<T>
) : Task<T> {

    @Volatile
    private var cancelled = false

    private var executed = false

    private var listenerCalled = AtomicBoolean(false)



    override fun await(): T {
        synchronized(this){
            if(cancelled) throw CancelledException()
            if(executed) throw IllegalStateException("Task has been executed")
            executed = true
        }
        // await is out of synchronized bloc of allow cancelling from another thread
        return task.await()
    }



    override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) = synchronized(this) {
        if(cancelled)return
        if(executed)throw IllegalStateException("Task has been executed")
        executed = true

        val finalListener: TaskListener<T> = {result->
            //this code block is not synchronized because is it launched after enqueue() method finishes
            if (listenerCalled.compareAndSet(false, true))
            {
                if (!cancelled) listener(result)
            }
        }
        task.enqueue(dispatcher, finalListener)
    }


    override fun cancel() = synchronized(this) {
        if (listenerCalled.compareAndSet(false, true)){
            if (cancelled) return@synchronized
            cancelled = true
            task.cancel()
        }

    }




}
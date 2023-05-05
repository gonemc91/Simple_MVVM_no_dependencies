package com.example.foundation.model.tasks

import com.example.foundation.model.ErrorResult
import com.example.foundation.model.FinalResult
import com.example.foundation.model.SuccessResult
import com.example.foundation.model.tasks.dispatchers.Dispatcher
import com.example.foundation.utils.delegates.Await
import kotlin.coroutines.cancellation.CancellationException

/**
 * Base class foe easier creation of new tasks.
 * Provides two method which should be implemented: [doEnqueue] and [doCancel]
 */


abstract class AbstractTask<T> : Task<T>{


    /**
     *Launch the task asynchronously. Listener should be called  when task when finished.
     *You may also use [executeBody] if you task executes [TaskBody] in some way.
     *
     */
    abstract fun doEnqueue(listener: TaskListener<T>)

    final override fun await(): T {
       val wrapperListener: TaskListener<T> = {
           finalResult=it
       }
        doEnqueue(wrapperListener)
        try {
            when(val result = finalResult){
                is ErrorResult-> throw result.exeption
                is SuccessResult-> return result.data
            }
        } catch (e: java.lang.Exception)  {
            if (e is InterruptedException){
                cancel()
                throw  CancellationException(e)
            } else{
                throw e
            }
        }
    }

    override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    private var finalResult by Await<FinalResult<T>>()

    /**
     * Cancel the task.
     */


    abstract fun doCancel()
}
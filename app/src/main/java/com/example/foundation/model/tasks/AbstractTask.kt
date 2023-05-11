package com.example.foundation.model.tasks

import com.example.foundation.model.ErrorResult
import com.example.foundation.model.FinalResult
import com.example.foundation.model.SuccessResult
import com.example.foundation.model.tasks.dispatchers.Dispatcher
import com.example.foundation.model.tasks.factories.TaskBody
import com.example.foundation.utils.delegates.Await
import kotlin.coroutines.cancellation.CancellationException

/**
 * Base class for easier creation of new tasks.
 * Provides two method which should be implemented: [doEnqueue] and [doCancel]
 */


abstract class AbstractTask<T> : Task<T>{


    private var finalResult by Await<FinalResult<T>>()

    /**
     *Launch the task asynchronously. Listener should be called  when task when finished.
     *You may also use [executeBody] if you task executes [TaskBody] in some way.
     *
     */


    final override fun await(): T {
       val wrapperListener: TaskListener<T> = {
           finalResult=it
       }
        doEnqueue(wrapperListener)
        try {
            when(val result = finalResult){
                is ErrorResult-> throw result.exception
                is SuccessResult-> return result.data
            }
        } catch (e: Exception)  {
            if (e is InterruptedException){
                cancel()
                throw  CancellationException(e)
            } else{
                throw e
            }
        }
    }

    final override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) {
        val wrapperListener: TaskListener<T> = {
            finalResult=it
            dispatcher.dispatch{
                listener(finalResult)
            }
        }
        doEnqueue(wrapperListener)
    }

    final override fun cancel() {
        finalResult = ErrorResult(CancelledException())
        doCancel()
    }

    fun executeBody(taskBody: TaskBody<T>, listener: TaskListener<T>){
        try{
            val data = taskBody()
            listener(SuccessResult(data))
        }catch (e: Exception){
            listener(ErrorResult(e))
        }
    }





    abstract fun doEnqueue(listener: TaskListener<T>)
    /**
     * Cancel the task.
     */
    abstract fun doCancel()
}
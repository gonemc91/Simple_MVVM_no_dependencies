package com.example.foundation.model.tasks.factories

import com.example.foundation.model.tasks.AbstractTask
import com.example.foundation.model.tasks.SynchronizedTask
import com.example.foundation.model.tasks.Task
import com.example.foundation.model.tasks.TaskListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


class ExecutorServiceTaskFactory(
    private val executorService: ExecutorService
) : TasksFactory {


    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ExecutorServiceTask(body))
    }

    private inner class  ExecutorServiceTask<T>(
        private val body: TaskBody<T>
    ): AbstractTask<T>(){

        private var future: Future<*>? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            future = executorService.submit{
                /*executeBody(body,listener)*/
            }

        }

        override fun enqueue(listener: TaskListener<T>) {
            TODO("Not yet implemented")
        }

        override fun doCancel() {
            future?.cancel(true)
        }


    }


}
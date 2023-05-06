package com.example.simple_mvvm

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.tasks.ThreadUtils
import com.example.foundation.model.tasks.dispatchers.MainThreadDispatcher
import com.example.foundation.model.tasks.factories.ExecutorServiceTaskFactory
import com.example.foundation.model.tasks.factories.HandlerThreadTasksFactory
import com.example.foundation.model.tasks.factories.ThreadTasksFactory
import com.example.simple_mvvm.model.colors.InMemoryColorsRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * Here we store instance of model layer classes
 */
class App : Application(), BaseApplication {

    private val singleThreadExecutorTaskFactory = ExecutorServiceTaskFactory(Executors.newSingleThreadExecutor())
    private val cashedThreadExecutorTaskFactory = ExecutorServiceTaskFactory(Executors.newCachedThreadPool())
    private  val handlerThreadTaskFactory = HandlerThreadTasksFactory()


    private val threadUtils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()

    /**
     * Place you repository here, now we have only one repository
     */


    override val singletonScopeDependencies: List<Any> = listOf(
        cashedThreadExecutorTaskFactory,
        dispatcher,

        InMemoryColorsRepository(handlerThreadTaskFactory, threadUtils)
    )

}
package com.example.simple_mvvm

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.tasks.ThreadUtils
import com.example.foundation.model.tasks.dispatchers.MainThreadDispatcher
import com.example.foundation.model.tasks.factories.ExecutorServiceTasksFactory
import com.example.foundation.model.tasks.factories.HandlerThreadTasksFactory
import com.example.simple_mvvm.model.colors.InMemoryColorsRepository
import java.util.concurrent.Executors


/**
 * Here we store instance of model layer classes
 */
class App : Application(), BaseApplication {


    // instances of all created task factories
    private val singleThreadExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newSingleThreadExecutor())
    private val handlerThreadTasksFactory = HandlerThreadTasksFactory()
    private val cachedThreadPoolExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newCachedThreadPool())


    private val threadUtils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()

    /**
     * Place you repository here, now we have only one repository
     */


    override val singletonScopeDependencies: List<Any> = listOf(
        cachedThreadPoolExecutorTasksFactory, // task factory to be used in view-models
        dispatcher, // dispatcher to be used in view-models

        InMemoryColorsRepository(cachedThreadPoolExecutorTasksFactory, threadUtils)
    )

}
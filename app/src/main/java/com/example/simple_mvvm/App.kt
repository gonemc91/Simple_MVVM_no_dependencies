package com.example.simple_mvvm

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.tasks.dispatchers.MainThreadDispatcher
import com.example.foundation.model.tasks.ThreadUtils
import com.example.simple_mvvm.model.colors.InMemoryColorsRepository


/**
 * Here we store instance 0f model layer classes
 */
class App : Application(), BaseApplication {

    private val tasksFactory = SimpleTasksFactory()

    private val threadUtils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()

    /**
     * Place you repository here, now we have only one repository
     */


    override val singletonScopeDependencies: List<Any> = listOf(
        tasksFactory,
        dispatcher,
        InMemoryColorsRepository(tasksFactory, threadUtils)
    )

}
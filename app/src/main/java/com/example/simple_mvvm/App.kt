package com.example.simple_mvvm

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.coroutines.IoDispatcher
import com.example.foundation.model.coroutines.WorkerDispatcher
import com.example.simple_mvvm.model.colors.InMemoryColorsRepository


/**
 * Here we store instance of model layer classes
 */
class App : Application(), BaseApplication {


    private val ioDispatcher = IoDispatcher()
    private val workerDispatcher = WorkerDispatcher()


    /**
     * Place you repository here, now we have only one repository
     */

    override val singletonScopeDependencies: List<Any> = listOf(
        ioDispatcher,
        workerDispatcher,

        InMemoryColorsRepository(ioDispatcher)
    )

}
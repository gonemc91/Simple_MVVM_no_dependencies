package com.example.simple_mvvm

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.Repository
import com.example.foundation.model.tasks.SimpleTasksFactory
import com.example.simple_mvvm.model.colors.InMemoryColorsRepository


/**
 * Here we store instance 0f model layer classes
 */
class App: Application(), BaseApplication {

    private val tasksFactory = SimpleTasksFactory()

    /**
     * Place you repository here, now we have only one repository
      */


override val repositories: List<Repository> = listOf(
        tasksFactory,
        InMemoryColorsRepository(tasksFactory)
    )

}
package com.example.simple_mvvm

import com.example.foundation.SingletonScopeDependencies
import com.example.foundation.model.coroutines.IoDispatcher
import com.example.foundation.model.coroutines.WorkerDispatcher
import com.example.simple_mvvm.model.colors.InMemoryColorsRepository

object Initializer {
    //Place you repository here, now we have only one repository
    fun initDependencies() = SingletonScopeDependencies.init { aplicationContext ->

        val ioDispatcher = IoDispatcher()
        val workerDispatcher = WorkerDispatcher()


        return@init listOf(
            ioDispatcher,
            workerDispatcher,

            InMemoryColorsRepository(ioDispatcher)
        )
    }
}

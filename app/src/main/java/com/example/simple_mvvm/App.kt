package com.example.simple_mvvm

import android.app.Application
import com.example.simple_mvvm.model.colors.InMemoryColorsRepository


/**
 * Here we store instance 0f model layer classes
 */
class App: Application() {

    /**
     * Place you repository here, now we have only one repository
      */


val models = listOf<Any>(
        InMemoryColorsRepository()
    )

}
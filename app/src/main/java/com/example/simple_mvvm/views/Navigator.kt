package com.example.simple_mvvm.views

import com.example.simple_mvvm.views.base.BaseScreen

/**
 * Navigator for you application
 */

interface Navigator {

    /**
     * Launch a new screen at the top of back stack
     */

    fun launch(screen: BaseScreen)

    /**
     * Go back to the previous screen and optionally send some result
     */

    fun goBack (result: Any? = null)

}
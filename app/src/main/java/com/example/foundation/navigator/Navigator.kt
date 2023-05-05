package com.example.foundation.navigator

import com.example.foundation.model.tasks.TaskListener
import com.example.foundation.views.BaseScreen

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
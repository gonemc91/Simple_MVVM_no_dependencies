package com.example.foundation.sideeffects.navigator.plugin

import android.content.Context
import com.example.foundation.sideeffects.SideEffectMediator
import com.example.foundation.sideeffects.SideEffectPlugin
import com.example.foundation.sideeffects.navigator.Navigator

/**
 * This plugins allows using [Navigator] interface for view-model.
 * This plugin may support different navigator implementation so you should pass the
 * desired navigator to the constructor. Now there is one default implementation: [StackFragmentNavigator].
 * Allows adding [Navigator] interface to view-model constructor.
 *
 *
 */

class NavigatorPlugin(
    private val navigator: Navigator
) : SideEffectPlugin<Navigator, Navigator> {
    override val mediatorClass: Class<Navigator>
        get() = Navigator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Navigator> {
        return SideEffectMediator()
    }

    override fun createImplementation(mediator: Navigator): Navigator? {
        return navigator
    }
}
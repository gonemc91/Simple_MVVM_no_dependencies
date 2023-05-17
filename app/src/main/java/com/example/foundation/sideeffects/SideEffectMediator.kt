package com.example.foundation.sideeffects

import com.example.foundation.model.dispatchers.Dispatcher
import com.example.foundation.model.dispatchers.MainThreadDispatcher
import com.example.foundation.utils.ResourceActions


/**
 * Base class for all side-effect mediators.
 * These mediators live in [ActivityScopeViewModel]
 * Mediator should delegate all UI-related logic to the implementations via [target] field.
 *
 */

open class SideEffectMediator<Implementation> (
    dispatcher: Dispatcher = MainThreadDispatcher()
){

    protected val target = ResourceActions<Implementation>(dispatcher)
    /**
     * Assign/unassigned the target implementation for this provider.
     */
    fun setTarget(target: Implementation?){
        this.target.resource = target
    }

    fun clear(){
        target.clear()
    }


}
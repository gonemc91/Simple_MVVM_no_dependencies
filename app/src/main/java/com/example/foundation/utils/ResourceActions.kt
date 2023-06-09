package com.example.foundation.utils

import android.annotation.SuppressLint
import java.util.concurrent.Executor


typealias ResourceAction<T> = (T) -> Unit
/**
 * Actions queue, where actions are executed only if resources exists. If it doesn't then
 * action is added to queue and waits until resource(.MainActivity lifecycle onResume) become available.
 */

class ResourceActions<T>(
    private val executor: Executor
) {

    var resource: T? = null
    set(newValue) {
        field = newValue
        if(newValue != null){
            actions.forEach{action->
                executor.execute {
                    action(newValue)
                }
            }
            actions.clear()
        }
    }
    private val actions = mutableListOf<ResourceAction<T>>()
    /**
     * Invoke this action only [resource] exists (not null). Otherwise
     * the action is postponed until some non-null value is assigned to [resource]
     */

@SuppressLint("SuspiciousIndentation")
operator fun invoke(action: ResourceAction<T>){
    val resource = this.resource
        if(resource == null) {
            actions += action
        }else{
            action(resource)
        }
}

     fun clear(){
        actions.clear()
    }

}
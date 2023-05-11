package com.example.foundation

import androidx.lifecycle.ViewModel
import com.example.foundation.sideeffects.SideEffectMediator
import com.example.foundation.sideeffects.SideEffectMediatorsHolder

/**
* Holder for side-effect mediators.
 *  It is based on activity view-model because instance of side-effect mediators
 *  should be available from fragments view-models (usually the are passed to the view-model constructor)
 *
*/

class ActivityScopeViewModel :  ViewModel() {

    internal val sideEffectMediatorsHolder = SideEffectMediatorsHolder()


    // contains thi list of side-effect mediators tht can be
    // passed to view-model constructor
    val sideEffectMediators: List<SideEffectMediator<*>>
    get() = sideEffectMediatorsHolder.mediators


    override fun onCleared() {
        super.onCleared()
        sideEffectMediatorsHolder.clear()
    }

}
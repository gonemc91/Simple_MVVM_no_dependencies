package com.example.foundation

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.foundation.navigator.IntermediateNavigator
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions

const val  ARGS_SCREEN = "ARGS_SCREEN"
/**
* Implementation of [Navigator] and [UiActions].
* It is based on activity view-model because instance of [Navigator] and [UiActions]
* should be available from fragments' view-model (usually they passed to the view-model constructor).
*
* This view-model extends [AndroidViewModel] which means that it not "usual" view-model and
* it may contain android dependencies (context, bundle, etc.)
*
*/

class ActivityScopeViewModel(
    val uiActions: UiActions,
    val navigator: IntermediateNavigator
): ViewModel(), Navigator by navigator, UiActions by uiActions {

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }

}
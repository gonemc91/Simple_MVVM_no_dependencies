package com.example.simple_mvvm.views.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simple_mvvm.model.utils.Event


typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

/**
 * Base class foe all viw-models
 */

open class BaseViewModel: ViewModel() {

    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */

    open fun onResult(result: Any){

    }


}
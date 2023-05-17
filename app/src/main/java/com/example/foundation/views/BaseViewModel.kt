package com.example.foundation.views

import androidx.lifecycle.*
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.Result
import com.example.foundation.model.SuccessResult
import com.example.foundation.utils.Event
import kotlinx.coroutines.launch



typealias  LiveResult<T> = LiveData<Result<T>>
typealias  MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias  MediatorLiveResult<T> = MediatorLiveData<Result<T>>

/**
 * Base class foe all viw-models
 */

open class BaseViewModel
: ViewModel() {


    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */

    override fun onCleared() {
        super.onCleared()
        clearTasks()

    }

    open fun onResult(result: Any){

    }

    fun onBackPressed(): Boolean{
        clearTasks()
        return false
    }


    fun <T> into(liveResult: MutableLiveResult<T>, block : suspend () -> T){
        viewModelScope.launch {
            try {
                liveResult.postValue(SuccessResult(block()))
            }catch (e: Exception){
                liveResult.postValue(ErrorResult(e))
            }
        }

    }


    private fun clearTasks(){
    }


}
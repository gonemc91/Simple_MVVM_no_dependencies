package com.example.foundation.views

import androidx.lifecycle.*
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.Result
import com.example.foundation.model.SuccessResult
import kotlinx.coroutines.*


typealias  LiveResult<T> = LiveData<Result<T>>
typealias  MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias  MediatorLiveResult<T> = MediatorLiveData<Result<T>>

/**
 * Base class foe all viw-models
 */

open class BaseViewModel: ViewModel() {

    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler{_, throwable ->
        // you can add some exception handling here

    }

    // custom scope which cancels jobs immediately when back button is pressed
    protected val viewModelScope = CoroutineScope(coroutineContext)


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
    /**
     * Launch the specified suspending [block] and use its result as a valud for the
     * provided [liveResult].
     */

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
        viewModelScope.cancel()
    }


}
package com.example.foundation.views

import androidx.lifecycle.*
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.Result
import com.example.foundation.model.SuccessResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect


typealias  LiveResult<T> = LiveData<Result<T>>
typealias  MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias  MediatorLiveResult<T> = MediatorLiveData<Result<T>>

/**
 * Base class foe all viw-models
 */

open class BaseViewModel: ViewModel() {

    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler {_, throwable ->
        // you can add some exception handling here

    }

    // custom scope which cancels jobs immediately when back button is pressed
    protected val viewModelScope: CoroutineScope = CoroutineScope(coroutineContext)



    override fun onCleared() {
        super.onCleared()
        clearViewModelScope()
    }
    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */

    open fun onResult(result: Any){

    }

    open fun onBackPressed(): Boolean{
      clearViewModelScope()
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
               if(e !is CancellationException) liveResult.postValue(ErrorResult(e))
            }
        }

    }
    fun <T> into(stateFlow: MutableStateFlow<Result<T>>, block : suspend () -> T){
        viewModelScope.launch {
            try {
                stateFlow.value = SuccessResult(block())
            }catch (e: Exception){
                if (e !is CancellationException) stateFlow.value = ErrorResult(e)
            }
        }

    }

    fun <T> SavedStateHandle.getStateFlowMy(key: String, initialValue: T): MutableStateFlow<T>{
        val savedStateHandle = this
        val mutableFlow = MutableStateFlow(savedStateHandle[key] ?: initialValue)

        viewModelScope.launch {
            mutableFlow.collect{
                savedStateHandle[key] = it
            }
        }

        viewModelScope.launch {
            savedStateHandle.getLiveData<T>(key).asFlow().collect{
                mutableFlow.value = it
            }
        }
        return mutableFlow
    }


    private fun clearViewModelScope(){
        viewModelScope.cancel()
    }


}
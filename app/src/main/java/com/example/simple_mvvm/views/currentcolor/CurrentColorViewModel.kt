package com.example.simple_mvvm.views.currentcolor

import com.example.foundation.model.PendingResult
import com.example.foundation.model.SuccessResult
import com.example.foundation.model.takeSuccess
import com.example.foundation.model.tasks.dispatchers.Dispatcher
import com.example.foundation.model.tasks.factories.TasksFactory
import com.example.foundation.sideeffects.dialogs.Dialogs
import com.example.foundation.sideeffects.intents.Intents
import com.example.foundation.sideeffects.navigator.Navigator
import com.example.foundation.sideeffects.resources.Resources
import com.example.foundation.sideeffects.toast.Toasts
import com.example.foundation.views.BaseViewModel
import com.example.foundation.views.LiveResult
import com.example.foundation.views.MutableLiveResult
import com.example.simple_mvvm.R
import com.example.simple_mvvm.model.colors.ColorListener
import com.example.simple_mvvm.model.colors.ColorsRepository
import com.example.simple_mvvm.model.colors.NamedColor
import com.example.simple_mvvm.views.changecolor.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val intents: Intents,
    private val dialogs: Dialogs,
    private val tasksFactory: TasksFactory,
    private val colorsRepository: ColorsRepository,
    dispatcher: Dispatcher
) : BaseViewModel(dispatcher) {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    var currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    //--- example of listening results via model layer

    init {
        colorsRepository.addListener(colorListener)
        load()

    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    //example of listening results directly from the screen
    override fun onResult(result: Any) {
        super.onResult(result)
        if(result is NamedColor){
            val message = resources.getString(R.string.changed_color, result.name)
            toasts.toast(message)
        }
    }
    //---

    fun changeColor(){
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun tryAgain(){
     load()
    }

    private fun load(){
        colorsRepository.getCurrentColor().into(_currentColor)
    }






}
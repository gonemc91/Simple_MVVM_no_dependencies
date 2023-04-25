package com.example.simple_mvvm.views.currentcolor

import androidx.lifecycle.viewModelScope
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.PendingResult
import com.example.foundation.model.SuccessResult
import com.example.foundation.model.takeSuccess
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions
import com.example.foundation.views.BaseViewModel
import com.example.foundation.views.LiveResult
import com.example.foundation.views.MutableLiveResult
import com.example.simple_mvvm.R
import com.example.simple_mvvm.model.colors.ColorListener
import com.example.simple_mvvm.model.colors.ColorsRepository
import com.example.simple_mvvm.model.colors.NamedColor
import com.example.simple_mvvm.views.changecolor.ChangeColorFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    var currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    //--- example of listening results via model layer

    init {
        viewModelScope.launch {
            delay(2000)
            _currentColor.postValue(ErrorResult(RuntimeException()))
        }
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    //example of listening results directly from the screen
    override fun onResult(result: Any) {
        super.onResult(result)
        if(result is NamedColor){
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }
    //---

    fun changeColor(){
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }



}
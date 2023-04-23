package com.example.simple_mvvm.views.currentcolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simple_mvvm.R
import com.example.simple_mvvm.model.colors.ColorListener

import com.example.simple_mvvm.model.colors.ColorsRepository
import com.example.simple_mvvm.model.colors.NamedColor
import com.example.simple_mvvm.views.Navigator
import com.example.simple_mvvm.views.UiActions
import com.example.simple_mvvm.views.base.BaseViewModel
import com.example.simple_mvvm.views.changecolor.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveData<NamedColor>()
    var currentColor: LiveData<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(it)
    }

    //--- example of listening results via model layer

    init {
        colorsRepository.addListener(colorListener)
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
        val currentColor = currentColor.value ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }



}
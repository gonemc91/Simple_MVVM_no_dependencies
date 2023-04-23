package com.example.simple_mvvm.views.changecolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.simple_mvvm.R
import com.example.simple_mvvm.model.colors.ColorsRepository
import com.example.simple_mvvm.model.colors.NamedColor
import com.example.simple_mvvm.views.Navigator
import com.example.simple_mvvm.views.UiActions
import com.example.simple_mvvm.views.base.BaseViewModel

class ChangeColorViewModel(
    screen: ChangeColorFragment.Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), ColorsAdapter.Listener{

    //input sources
    private  val _availableColors = MutableLiveData<List<NamedColor>>()
    private val _currentColorId = savedStateHandle.getLiveData("currentColorId", screen.currentColorId)

    //main destination (contains merged values from _availableColors & _currentColorId)
    private val _colorsList = MediatorLiveData<List<NamedColorListItem>>()
    var colorsList: LiveData<List<NamedColorListItem>> = _colorsList


    //side destination, also the same result can be achieved by using Transformations.map() function
    private val _screenTitle = MutableLiveData<String>()
    var screenTitle: LiveData<String> = _screenTitle

    init {
        _availableColors.value = colorRepository.getAvailableColors()
        // initializing MediatorLiveData
        _colorsList.addSource(_availableColors){ mergeSources()}
        _colorsList.addSource(_currentColorId){ mergeSources()}
    }




    fun onSavedPressed(){
        val currentColorId = _currentColorId.value ?: return
        val currentColor = colorRepository.getById(currentColorId)
        colorRepository.currentColor = currentColor
        navigator.goBack(result =  currentColor)
    }

     fun onCancelPressed(){
        navigator.goBack()
    }



    override fun onColorChosen(namedColor: NamedColor) {
        _currentColorId.value = namedColor.id
    }

    /**
     * [MediatorLiveData] can listen other LiveData instances (even more than 1)
     * and combine their values.
     * Here we listen the list of available colors ([_availableColors] live-data) + current color id
     * ([_currentColorId] live-data), then we use both of these values in order to create a list of
     * [NamedColorListItem], it is a list to be displayed in RecyclerView.
     */


    private fun mergeSources(){
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val currentColor = colors.first{ it.id == currentColorId}
        _colorsList.value = colors.map { NamedColorListItem(it, currentColorId == it.id) }
        _screenTitle.value = uiActions.getString(R.string.change_color_screen_title, currentColor.name)
    }
}
package com.example.simple_mvvm.views.changecolor

import androidx.lifecycle.*
import com.example.foundation.model.ErrorResult
import com.example.foundation.model.FinalResult
import com.example.foundation.model.PendingResult
import com.example.foundation.model.SuccessResult
import com.example.foundation.model.tasks.dispatchers.Dispatcher
import com.example.foundation.model.tasks.factories.TasksFactory
import com.example.foundation.uiactions.UiActions
import com.example.foundation.views.BaseViewModel
import com.example.foundation.views.LiveResult
import com.example.foundation.views.MediatorLiveResult
import com.example.foundation.views.MutableLiveResult
import com.example.simple_mvvm.R
import com.example.simple_mvvm.model.colors.ColorsRepository
import com.example.simple_mvvm.model.colors.NamedColor

class ChangeColorViewModel(
    screen: ChangeColorFragment.Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorRepository: ColorsRepository,
    private val tasksFactory: TasksFactory,
    savedStateHandle: SavedStateHandle,
    dispatcher: Dispatcher
) : BaseViewModel(dispatcher), ColorsAdapter.Listener{

    //input sources
    private  val _availableColors = MutableLiveResult<List<NamedColor>>(PendingResult())
    private val _currentColorId = savedStateHandle.getLiveData("currentColorId", screen.currentColorId)
    private val _saveInProgress = MutableLiveData(false)

    //main destination (contains merged values from _availableColors & _currentColorId)
    private val _viewState = MediatorLiveResult<ViewState>()
    var viewState: LiveResult<ViewState> = _viewState



    //side destination, also the same result can be achieved by using Transformations.map() function
    var screenTitle: LiveData<String> = viewState.map { result->
        if (result is SuccessResult){
            val currentColor = result.data.colorsList.first{it.selected}
            uiActions.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
        }else{
            uiActions.getString(R.string.change_color_screen_title_simple)
        }
    }

    init {
        load()
        // initializing MediatorLiveData
        _viewState.addSource(_availableColors){ mergeSources()}
        _viewState.addSource(_currentColorId){ mergeSources()}
        _viewState.addSource(_saveInProgress){mergeSources()}
    }




    fun onSavedPressed(){
        _saveInProgress.postValue(true)

        tasksFactory.async {
            //this code is launched asynchronously in other thread
            val currentColorId = _currentColorId.value ?: throw IllegalStateException("Color ID should not be NULL")
            val currentColor = colorRepository.getById(currentColorId).await()
            colorRepository.setCurrentColor(currentColor).await()
            return@async currentColor
        }
                // method is called in main thread when async code is finished
            .safeEnqueue(::onSaved)
    }

     fun onCancelPressed(){
        navigator.goBack()
    }



    override fun onColorChosen(namedColor: NamedColor) {
        if(_saveInProgress.value == true) return
        _currentColorId.value = namedColor.id
    }

    fun tryAgain() {
     load()
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
        val saveInProgress = _saveInProgress.value ?: return

        _viewState.value = colors.map{colorsList->
            ViewState(
                colorsList =  colorsList.map { NamedColorListItem(it, currentColorId == it.id)},
                showSaveButton = !saveInProgress,
                showCancelButton = !saveInProgress,
                showSaveProgressBar = saveInProgress
            )
           }
        }

    private fun load(){
        colorRepository.getAvailableColors().into(_availableColors)
    }
    private fun onSaved(result: FinalResult<NamedColor>){
        _saveInProgress.value = false
        when(result){
            is SuccessResult -> navigator.goBack(result.data)
            is ErrorResult -> uiActions.toast(uiActions.getString(R.string.error_happened))
        }

    }
    }

    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean
    )

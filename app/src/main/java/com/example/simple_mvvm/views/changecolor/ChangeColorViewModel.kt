package com.example.simple_mvvm.views.changecolor

import androidx.constraintlayout.motion.utils.ViewState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.example.foundation.model.*
import com.example.foundation.sideeffects.navigator.Navigator
import com.example.foundation.sideeffects.resources.Resources
import com.example.foundation.sideeffects.toast.Toasts
import com.example.foundation.views.BaseViewModel
import com.example.simple_mvvm.R
import com.example.simple_mvvm.model.colors.ColorsRepository
import com.example.simple_mvvm.model.colors.NamedColor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChangeColorViewModel(
    screen: ChangeColorFragment.Screen,
    private val toasts: Toasts,
    private  val navigator: Navigator,
    private val resource: Resources,
    private val colorRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    //input sources
    private val _availableColors = MutableStateFlow <Result<List<NamedColor>>>(PendingResult())
    private val _currentColorId = savedStateHandle.getStateFlowMy("currentColorId", screen.currentColorId)
    private val _saveInProgress = MutableStateFlow<Progress>(EmptyProgress)

    //main destination (contains merged values from _availableColors & _currentColorId)

    var viewState: Flow<Result<ViewState>> = combine(
        _availableColors,
        _currentColorId,
        _saveInProgress,
        ::mergeSources
    )


    //side destination, also the same result can be achieved by using Transformations.map() function
    var screenTitle: LiveData<String> = viewState
        .map { result ->
            return@map if (result is SuccessResult) {
                val currentColor = result.data.colorsList.first { it.selected }
                resource.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
            } else {
                resource.getString(R.string.change_color_screen_title_simple)
            }
        }.asLiveData()

    init {
        load()
    }


    fun onSavedPressed() = viewModelScope.launch {

        try {
            _saveInProgress.value = PercentageProgress.START
            //this code is launched asynchronously in other thread
            val currentColorId =
                _currentColorId.value
            val currentColor = colorRepository.getById(currentColorId)

            colorRepository.setCurrentColor(currentColor).collect{percentange->
                _saveInProgress.value = PercentageProgress(percentange)
            }
            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e !is CancellationException) toasts.toast(resource.getString(R.string.error_happened))
        } finally {
            _saveInProgress.value = EmptyProgress
        }
    }

    fun onCancelPressed() {
        navigator.goBack()
    }


    override fun onColorChosen(namedColor: NamedColor) {
        if (_saveInProgress.value.isInProgress()) return
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


    private fun mergeSources(colors: Result<List<NamedColor>>, currentColorId: Long, saveInProgress: Progress) : Result<ViewState> {


        return colors.map { colorsList ->
            ViewState(
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },
                showSaveButton = !saveInProgress.isInProgress(),
                showCancelButton = !saveInProgress.isInProgress(),
                showSaveProgressBar = saveInProgress.isInProgress(),


                saveProgressPercentage = saveInProgress.getPercentage(),
                saveProgressPercentageMessage = resource.getString(R.string.percentage_value, saveInProgress.getPercentage())
            )
        }
    }

    private fun load() = into(_availableColors) { return@into colorRepository.getAvailableColors() }


    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean,

        val saveProgressPercentage: Int,
        val saveProgressPercentageMessage: String
    )
}

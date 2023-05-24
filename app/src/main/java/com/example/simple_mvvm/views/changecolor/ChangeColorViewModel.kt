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
import com.example.foundation.utils.finiteShareIn
import com.example.foundation.views.BaseViewModel
import com.example.simple_mvvm.R
import com.example.simple_mvvm.model.colors.ColorsRepository
import com.example.simple_mvvm.model.colors.NamedColor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
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
    private val _instantSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)
    private val _sampleSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)





    //main destination (contains merged values from _availableColors & _currentColorId)

    var viewState: Flow<Result<ViewState>> = combine(
        _availableColors,
        _currentColorId,
        _instantSaveInProgress,
        _sampleSaveInProgress,
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
            _instantSaveInProgress.value = PercentageProgress.START
            _sampleSaveInProgress.value = PercentageProgress.START
            //this code is launched asynchronously in other thread
            val currentColorId =
                _currentColorId.value
            val currentColor = colorRepository.getById(currentColorId)

            val flow = colorRepository.setCurrentColor(currentColor)
                .finiteShareIn(this)

            val instantJob = async {
                flow.collect { percentange ->
                    _instantSaveInProgress.value = PercentageProgress(percentange)
                }
            }
            val simpleJob = async {
                flow.sample(200).collect { percentange ->
                    _sampleSaveInProgress.value = PercentageProgress(percentange)
                }
            }
            instantJob.await()
            simpleJob.await()


            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e !is CancellationException) toasts.toast(resource.getString(R.string.error_happened))
        } finally {
            _instantSaveInProgress.value = EmptyProgress
            _sampleSaveInProgress.value = EmptyProgress
        }
    }

    fun onCancelPressed() {
        navigator.goBack()
    }


    override fun onColorChosen(namedColor: NamedColor) {
        if (_instantSaveInProgress.value.isInProgress()) return
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


    private fun mergeSources(colors: Result<List<NamedColor>>, currentColorId: Long, instantSaveInProgress: Progress,sampleSaveInProgress: Progress) : Result<ViewState> {


        return colors.map { colorsList ->
            ViewState(
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },
                showSaveButton = !instantSaveInProgress.isInProgress(),
                showCancelButton = !instantSaveInProgress.isInProgress(),
                showSaveProgressBar = instantSaveInProgress.isInProgress(),


                saveProgressPercentage = instantSaveInProgress.getPercentage(),
                saveProgressPercentageMessage = resource.getString(R.string.percentage_value, sampleSaveInProgress.getPercentage())
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

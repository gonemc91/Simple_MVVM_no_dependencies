package com.example.simple_mvvm.views.changecolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.HasScreenTitle
import com.example.foundation.views.screenViewModel
import com.example.simple_mvvm.R
import com.example.simple_mvvm.databinding.FragmentChangeColorBinding
import com.example.simple_mvvm.views.collectFlow
import com.example.simple_mvvm.views.onTryAgain
import com.example.simple_mvvm.views.renderSimpleResult


/**
 * Screen for changing color.
 * 1) Display the list of available colors
 * 2) Allows choosing the desired color
 * 3) Chosen color is saved only after pressing "Save" button
 * 4) The current choice is saved via [SavedStateHandle] (see[ChangeColorViewModel])
 */


class ChangeColorFragment: BaseFragment(), HasScreenTitle {

    /**
     * This screen has 1 argument: colors ID to be displayed as selected
     */

    class Screen(
        val currentColorId: Long
    ) : BaseScreen

    override val viewModel by screenViewModel<ChangeColorViewModel>()


    /**
     *Example of dynamic screen title
     */

    override fun getScreenTitle(): String? = viewModel.screenTitle.value

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChangeColorBinding.inflate(inflater, container, false)

        val adapter = ColorsAdapter(viewModel)
        setupLayoutManger(binding, adapter)

        binding.saveButton.setOnClickListener {viewModel.onSavedPressed()}
        binding.cancelButton.setOnClickListener {viewModel.onCancelPressed()}


        collectFlow(viewModel.viewState){result->
            renderSimpleResult(binding.root, result){viewState ->
                adapter.items = viewState.colorsList
                binding.saveButton.visibility = if (viewState.showSaveButton) View.VISIBLE else View.INVISIBLE
                binding.cancelButton.visibility = if (viewState.showCancelButton) View.VISIBLE else View.INVISIBLE

                binding.savedProgressGroup.visibility = if (viewState.showSaveProgressBar) View.VISIBLE else View.GONE
                binding.saveProgressBar.progress = viewState.saveProgressPercentage
                binding.savingPercentageTextView.text = viewState.saveProgressPercentageMessage
            }
        }

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            // if screen title is changed -> need to notify activity about updates
            notifyScreenUpdates()
        }
        onTryAgain(binding.root){
            viewModel.tryAgain()
        }
        return binding.root
    }
    private fun setupLayoutManger(binding: FragmentChangeColorBinding, adapter: ColorsAdapter) {
        //waiting for list width
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.width
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / itemWidth
                binding.colorRecyclerView.adapter = adapter
                binding.colorRecyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
            }
        })

    }


}
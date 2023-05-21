package com.example.simple_mvvm.views

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.foundation.views.BaseFragment
import com.example.foundation.model.Result
import com.example.simple_mvvm.R
import com.example.simple_mvvm.databinding.PartResultBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> BaseFragment.renderSimpleResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit){
    val binding = PartResultBinding.bind(root)
    renderResult(
       root = root,
       result = result,
       onPending =  {
           binding.progressBar.visibility = View.VISIBLE
       },
        onError = {
            binding.containerError.visibility = View.VISIBLE
        },
        onSuccess = {successData->
            root.children
                .filter { it.id != R.id.progressBar && it.id != R.id.containerError}
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        }

    )
}

fun BaseFragment.onTryAgain(root: View, onTryAgainPressed: () -> Unit) {
    root.findViewById<Button>(R.id.tryAgainButton).setOnClickListener {
        onTryAgainPressed()
    }
}
    /**
     * Collect items from the specified [Flow] only when fragment is at least in STARTED state.
     */

    fun <T> BaseFragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            // this coroutines is cancelled in onDestroyView
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // this coroutines is launched every time when onStart is called;
                // collecting is cancelled in onStop
                flow.collect {
                    onCollect(it)
                }
            }

        }
    }


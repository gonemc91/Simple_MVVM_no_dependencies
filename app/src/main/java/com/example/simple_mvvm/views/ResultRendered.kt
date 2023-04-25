package com.example.simple_mvvm.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.example.foundation.views.BaseFragment
import com.example.foundation.model.Result
import com.example.simple_mvvm.R
import com.example.simple_mvvm.databinding.PartResultBinding

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
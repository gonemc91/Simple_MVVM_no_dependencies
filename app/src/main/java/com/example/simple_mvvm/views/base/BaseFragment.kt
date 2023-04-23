package com.example.simple_mvvm.views.base

import androidx.fragment.app.Fragment
import com.example.simple_mvvm.MainActivity

/**
 * Base class for all fragment
 */

abstract class BaseFragment: Fragment() {
    /**
     * View-model that manager this fragment
     *
     */
    abstract val viewModel: BaseViewModel

    /**
     * Call this method when activity controls (e.g. toolbar) should be re-rendered
     */

    fun notifyScreenUpdates(){
        // if you have more than 1 activity -> you should use a separate interface instead of direct
        // cast to MainActivity
        (requireActivity() as MainActivity).notifyScreenUpdates()
    }




}
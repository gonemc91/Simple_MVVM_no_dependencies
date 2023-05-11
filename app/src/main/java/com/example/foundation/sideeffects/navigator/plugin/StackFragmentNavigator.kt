package com.example.foundation.sideeffects.navigator.plugin

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.example.foundation.ARGS_SCREEN
import com.example.foundation.sideeffects.SideEffectImplementation
import com.example.foundation.sideeffects.navigator.Navigator
import com.example.foundation.utils.Event
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.HasScreenTitle


class StackFragmentNavigator(
    @IdRes private val containerId: Int,
    private val defaultTitle: String,
    private val animation: Animations,
    private val initialScreenCreator: () -> BaseScreen,
    override val lifecycle: Lifecycle
) : Navigator, SideEffectImplementation(), LifecycleOwner {

    private var result: Event<Any>? = null

    override fun launch(screen: BaseScreen) {
        launchFragment(screen)
    }

    override fun goBack(result: Any?) {
        if (result != null) {
            this.result = Event(result)
        }
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            //define the initial screen that should be launched when app starts.
            launchFragment(
                screen = initialScreenCreator(),
                addToBackStack = false
            )
        }
            requireActivity().supportFragmentManager.registerFragmentLifecycleCallbacks(
            fragmentsCallbacks,
            false
        )
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        requireActivity().supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentsCallbacks)
    }

    override fun onBackPressed(): Boolean {
        val f = getCurrentFragment()
        return if (f is BaseFragment) {
            f.viewModel.onBackPressed()
            true
        } else {
            false
        }
    }

    override fun onSupportNavigateUP(): Boolean? {
       requireActivity().onBackPressedDispatcher.onBackPressed()
        return true
    }


    fun launchFragment(screen: BaseScreen, addToBackStack: Boolean = true) {
        // as screen classes are inside fragments -> we can create fragment directly from screen
        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        //set screen object as fragment's argument
        fragment.arguments = bundleOf(ARGS_SCREEN to screen)

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .setCustomAnimations(
                animation.enterAnim,
                animation.exitAnim,
                animation.popEnterAnim,
                animation.popExitAnim
            )
            .replace(containerId, fragment)
            .commit()

    }

    override fun onRequestUpdates() {
        val f = getCurrentFragment()

        if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
            //more than 1 screen -> show back button in the toolbar
            requireActivity().supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            requireActivity().supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if (f is HasScreenTitle && f.getScreenTitle() != null) {
            //fragment has custom screen title -> display it
            requireActivity().supportActionBar?.title = f.getScreenTitle()
        } else {
            requireActivity().supportActionBar?.title = defaultTitle
        }
    }

    private fun publishResults(fragmet: Fragment){
        val result = result?.getValue() ?: return
        if (fragmet is BaseFragment) {
            //has result that can be delivered to the screen's view- model
            fragmet.viewModel.onResult(result)
        }
    }
    private fun getCurrentFragment(): Fragment?{
        return requireActivity().supportFragmentManager.findFragmentById(containerId)
    }


    private val fragmentsCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            onRequestUpdates()
            publishResults(f)

        }
    }

    class Animations(
        @AnimRes val enterAnim: Int,
        @AnimRes val exitAnim: Int,
        @AnimRes val popEnterAnim: Int,
        @AnimRes val popExitAnim: Int,

        )

}
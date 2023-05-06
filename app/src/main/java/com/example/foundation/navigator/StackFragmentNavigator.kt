package com.example.foundation.navigator

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.foundation.ARGS_SCREEN
import com.example.foundation.utils.Event
import com.example.foundation.views.BaseFragment
import com.example.foundation.views.BaseScreen
import com.example.foundation.views.HasScreenTitle

class StackFragmentNavigator(
    private val activity: AppCompatActivity,
    @IdRes private val containerId: Int,
    private val defaultTitle: String,
    private val animation: Animations,
    private val initialScreenCreator: () -> BaseScreen
) : Navigator {

    private var result: Event<Any>? = null

    override fun launch(screen: BaseScreen) {
        launchFragment(screen)
    }

    override fun goBack(result: Any?) {
        if (result != null) {
            this.result = Event(result)
        }
        activity.onBackPressedDispatcher.onBackPressed()
    }


    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            //define the initial screen that should be launched when app starts.
            launchFragment(
                screen = initialScreenCreator(),
                addToBackStack = false
            )
        }
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
            fragmentsCallbacks,
            false
        )
    }


    fun onDestroy() {
        activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentsCallbacks)
    }
    fun onBackPressed(){
        val f = getCurrentFragment()
        if (f is BaseFragment){
            f.viewModel.onBackPressed()
        }

    }


    fun launchFragment(screen: BaseScreen, addToBackStack: Boolean = true) {
        // as screen classes are inside fragments -> we can create fragment directly from screen
        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        //set screen object as fragment's argument
        fragment.arguments = bundleOf(ARGS_SCREEN to screen)

        val transaction = activity.supportFragmentManager.beginTransaction()
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

    fun notifyScreenUpdates() {
        val f = getCurrentFragment()

        if (activity.supportFragmentManager.backStackEntryCount > 0) {
            //more than 1 screen -> show back button in the toolbar
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if (f is HasScreenTitle && f.getScreenTitle() != null) {
            //fragment has custom screen title -> display it
            activity.supportActionBar?.title = f.getScreenTitle()
        } else {
            activity.supportActionBar?.title = defaultTitle
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
        return activity.supportFragmentManager.findFragmentById(containerId)
    }


    private val fragmentsCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            notifyScreenUpdates()
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
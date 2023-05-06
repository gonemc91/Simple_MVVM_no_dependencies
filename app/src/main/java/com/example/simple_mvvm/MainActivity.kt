package com.example.simple_mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foundation.ActivityScopeViewModel
import com.example.foundation.navigator.IntermediateNavigator
import com.example.foundation.navigator.StackFragmentNavigator
import com.example.foundation.uiactions.AndroidUIActions
import com.example.foundation.utils.viewModelCreator
import com.example.foundation.views.FragmentsHolder
import com.example.simple_mvvm.databinding.ActivityMainBinding
import com.example.simple_mvvm.views.currentcolor.CurrentColorFragment


/**
 * This application is a single-activity app. MainActivity is a container
 * for all screens
 */


class MainActivity : AppCompatActivity(), FragmentsHolder {
    private lateinit var navigator: StackFragmentNavigator

    private lateinit var binding: ActivityMainBinding

    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            uiActions = AndroidUIActions(applicationContext),
            navigator = IntermediateNavigator()
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)

        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragmentContainer,
            defaultTitle = getString(R.string.app_name),
            animation = StackFragmentNavigator.Animations(
                enterAnim = R.anim.enter,
                exitAnim = R.anim.exit,
                popEnterAnim = R.anim.pop_enter,
                popExitAnim = R.anim.pop_exit
            ),
            initialScreenCreator = { CurrentColorFragment.Screen()}

        )
        navigator.onCreate(savedInstanceState)

        }

    override fun onDestroy() {
        navigator.onDestroy()
        super.onDestroy()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        activityViewModel.navigator.setTarget(navigator)

    }

    override fun onPause() {
        super.onPause()
        //postpone navigation actions if activity is not active
        activityViewModel.navigator.setTarget(null)
    }

    override fun notifyScreenUpdates() {
        navigator.notifyScreenUpdates()
    }

    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
       return activityViewModel
    }

    override fun onBackPressed() {
        navigator.onBackPressed()
        super.onBackPressedDispatcher.onBackPressed()

    }
}

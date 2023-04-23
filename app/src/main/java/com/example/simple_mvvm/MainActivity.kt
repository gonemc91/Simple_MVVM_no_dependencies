package com.example.simple_mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.simple_mvvm.databinding.ActivityMainBinding
import com.example.simple_mvvm.views.HasScreenTitle
import com.example.simple_mvvm.views.base.BaseFragment
import com.example.simple_mvvm.views.currentcolor.CurrentColorFragment


/**
 * This application is a single-activity app. MainActivity is a container
 * for all screens
 */


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val  activityViewModel by viewModels<MainViewModel>{ViewModelProvider.AndroidViewModelFactory(application)}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)

        if (savedInstanceState == null){
           //define the initial screen that should be launched when app starts.
            activityViewModel.launchFragment(
                activity = this,
                screen = CurrentColorFragment.Screen(),
                addToBackStack = false
            )
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentsCallbacks, false)


    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentsCallbacks)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        activityViewModel.whenActivityActive.resource = this
    }

    override fun onPause() {
        super.onPause()
        //postpone navigation actions if activity is not active
        activityViewModel.whenActivityActive.resource = null
    }



    fun notifyScreenUpdates(){
        val f = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if(supportFragmentManager.backStackEntryCount>0){
            //more than 1 screen -> show back button in the toolbar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }else{
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if (f is HasScreenTitle && f.getScreenTitle() != null){
            //fragment has custom screen title -> display it
            supportActionBar?.title = f.getScreenTitle()
        }else{
            supportActionBar?.title = getString(R.string.app_name)
        }

        val result = activityViewModel.result.value?.getValue() ?: return
        if(f is BaseFragment){
            //has result that can be delivered to the screen's view- model
            f.viewModel.onResult(result)
        }

    }

    private val fragmentsCallbacks = object : FragmentManager.FragmentLifecycleCallbacks(){
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            notifyScreenUpdates()
        }
    }

}
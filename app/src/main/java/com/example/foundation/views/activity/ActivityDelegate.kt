package com.example.foundation.views.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import com.example.foundation.ActivityScopeViewModel
import com.example.foundation.sideeffects.SideEffectPlugin
import com.example.foundation.sideeffects.SideEffectPluginsManager
import com.example.foundation.sideeffectso.SideEffectImplementationsHolder
import com.example.foundation.utils.viewModelCreator


/**
 * Delegate that manages side-effect plugins and contains common logic.
 * The following methods should be called from activity:
 * - [onBackPressed]
 * - [onSupportNavigateUp]
 * - [onCreate]
 * - [onSavedInstanceState]
 * - [onActtivityResult]
 * - [onRequestPermissionResult]
 *
 */

class ActivityDelegate(
    private val activity: AppCompatActivity
) : DefaultLifecycleObserver {

    internal val sideEffectPluginManager = SideEffectPluginsManager()

    private val activityViewModel by activity.viewModelCreator<ActivityScopeViewModel> { ActivityScopeViewModel() }

    private val implementerHolder = SideEffectImplementationsHolder()


    init {
        activity.lifecycle.addObserver(this)
    }

    /**
     * Call this method from [AppCompatActivity.onBackPressed]
     * Example:
     * '''''
     * override fun onBackPressed(){
     *    if(!delegate.onBackPressed()) super.onBackPressed()
     *}
     *''''''
     */



    fun onBackPressed() : Boolean{
        return implementerHolder.implementations.any{it.onBackPressed()}
    }



    /**
     * Call this method from [AppCompatActivity.onSupportNavigateUp]
     * If this method returns 'null' you should call 'super.onSupportNavigateUp' if you activity.
     * Example:
     * ''''
     * override fun on SupportNavigateUp(): Boolean{
     *  return delegate.onSupportNavigateUp() ?: super.onSupportNavigateUp()
     * }
     * ''''
     *
     */


    fun onSupportNavigateUp(): Boolean?{
        for(service in implementerHolder.implementations){
            val value = service.onSupportNavigateUp()
            if(value != null){
                return value
            }
        }
        return null
    }

    /**
     * Call this method from [AppCompactActivity.onCreate].
     */

    fun onCreate(savedInstanceState: Bundle?){
        sideEffectPluginManager.plugins.forEach{plugin->
            setupSideEffectMediator(plugin)
            setupSideEffectImplementer(plugin)
        }
    }

    /**
     * Call this method from [AppCompatActivity.onSaveInstanceState]
     *
     */
    fun onSavedInstanceState(outState: Bundle){
        implementerHolder.implementations.forEach{it.onSavedInstanceState(outState)}
    }

    /**
     * Call this method from [AppCompatActivity.onActivityResult]
     */
    fun onActivityResult(requestCode: Int, responseCode: Int, data: Intent?){
        implementerHolder.implementations.forEach{it.onActivityResult(requestCode, responseCode, data)}
    }

    fun onRequestPermissionsResult(requestCode: Int, permission: Array<out String>, granted: IntArray){
        implementerHolder.implementations.forEach{ it.onRequestPermissionsResult(requestCode, permission, granted)
        }
    }

    fun notifyScreenUpdates(){
        implementerHolder.implementations.forEach{it.onRequestUpdates()}
    }

    fun getActivityScopeViewModel(): ActivityScopeViewModel{
        return activityViewModel
    }

   @Override
    private fun onResume(){
        sideEffectPluginManager.plugins.forEach{
            activityViewModel.sideEffectMediatorsHolder.removeTargets()
        }
    }

    @Override
    private fun onPause(){
        activityViewModel.sideEffectMediatorsHolder.removeTargets()
    }



    @Override
    private fun onDestroy() {
        implementerHolder.clear()
    }


    private fun setupSideEffectMediator(plugin: SideEffectPlugin<*, *>) {
        val holder = activityViewModel.sideEffectMediatorsHolder

        if(!holder.contains(plugin.mediatorClass)){
            holder.putWithPlugin(activity.applicationContext, plugin)
        }
    }

    private fun setupSideEffectImplementer(plugin: SideEffectPlugin<*,*>){
        implementerHolder.putWithPlugin(plugin, activityViewModel.sideEffectMediatorsHolder, activity)
    }


}
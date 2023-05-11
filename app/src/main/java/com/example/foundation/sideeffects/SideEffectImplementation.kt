package com.example.foundation.sideeffects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


/**
 * Base class for side-effect implementations.
 * Implementations are responsible for the real implementations of side- effect taling into
 * account activity lifecycle. You may safely launch dialogs, perform navigation, change UI here.
 * Implementations are tied to activity.
 *
 */

abstract class SideEffectImplementation {

    private lateinit var activity: AppCompatActivity

    fun requireActivity(): AppCompatActivity = activity

    open fun onCreate(savedInstanceState: Bundle?){}
    open fun onBackPressed() : Boolean { return false}
    open fun onRequestUpdates() {}
    open fun onSupportNavigateUP() : Boolean? {
        return null
    }

    open fun onSavedInstanceState(outBundle: Bundle){}
    open fun onActivityResult(requestCode: Int, permission: Array<out String>, granted: IntArray) {}
    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, granted: IntArray) {}

    internal fun injectActivity(activity: AppCompatActivity){
        this.activity = activity
    }
}
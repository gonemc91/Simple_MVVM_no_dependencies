package com.example.foundation.sideeffects.toast.plugin

import android.content.Context
import com.example.foundation.model.tasks.dispatchers.MainThreadDispatcher
import com.example.foundation.sideeffects.SideEffectMediator
import com.example.foundation.sideeffects.toast.Toasts
import android.widget.Toast
/**
 *Android implementation of [Toasts]. Displaying simple toast message and getting string from resources.
 */

class ToastsSideEffectMediator (
    private val appContext: Context
    ) : SideEffectMediator<Nothing>(), Toasts {


    private val dispatcher = MainThreadDispatcher()
    override fun toast(message: String) {
        dispatcher.dispatch {
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}

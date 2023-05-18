package com.example.foundation.sideeffects.toast.plugin

import android.content.Context
import com.example.foundation.sideeffects.SideEffectMediator
import com.example.foundation.sideeffects.toast.Toasts
import android.widget.Toast
import com.example.foundation.utils.MainThreadExecutor

/**
 *Android implementation of [Toasts]. Displaying simple toast message and getting string from resources.
 */

class ToastsSideEffectMediator (
    private val appContext: Context
    ) : SideEffectMediator<Nothing>(), Toasts {


    private val executor = MainThreadExecutor()
    override fun toast(message: String) {
        executor.execute {
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}

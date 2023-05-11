package com.example.foundation.sideeffects.toast.plugin

import android.content.Context
import com.example.foundation.sideeffects.SideEffectMediator
import com.example.foundation.sideeffects.SideEffectPlugin
import com.example.foundation.sideeffects.toast.Toasts

/**
 * Plugin for displaying toast message from view-models.
 * Allows adding [Toasts] interface to the view-model constructor.
 *
 */

class ToastsPlugin : SideEffectPlugin<Toasts, Nothing> {

    override val mediatorClass: Class<Toasts>
        get() = Toasts::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Nothing> {
        return ToastsSideEffectMediator(applicationContext)
    }
}
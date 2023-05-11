package com.example.foundation.sideeffects.toast

/**
 *Interface for showing toast message to the user from view-models
 * You need to add [ToastPlugin] to you activity before using the feature
 */


interface Toasts {

    /**
     *  Display a simple toast message.
     */

    fun toast(message: String)
}
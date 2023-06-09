package com.example.foundation.sideeffects.intents

/**
 * Side-effect interface for launching some system activities.
 * You need to add [IntentsPlugin] to your activity before using this feature.
 */




interface Intents {

    /**
     *
     *Open system settings for this application
     */
    fun openAppSettings()
}
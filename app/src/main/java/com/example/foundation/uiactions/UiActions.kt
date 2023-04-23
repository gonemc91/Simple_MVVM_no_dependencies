package com.example.foundation.uiactions

interface UiActions {

    /**
     * Display a simple toast message
     */
    fun toast(message: String)

    /**
     * Get string resources content by its identifier
     */

    fun getString(messageRes: Int, vararg args: Any): String
}
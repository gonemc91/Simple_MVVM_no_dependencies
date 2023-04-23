package com.example.simple_mvvm.model.colors

import com.example.simple_mvvm.model.Repository

typealias ColorListener = (NamedColor) -> Unit

/**
 * Repository interface example
 *
 * Provides access to the available colors and current selected colors
 */

interface ColorsRepository: Repository {

    var currentColor: NamedColor

    /**
     * Get the list of all available colors that that may be chosen by the user
     */
    fun getAvailableColors(): List<NamedColor>

    /**
     * Get the color content by its ID
     */
    fun getById(id: Long): NamedColor

    /**
     * Listen for the current color changes
     * The listener triggered immediately whits the current value when calling this method
     *
     */
    fun addListener(listener: ColorListener)


    /**
     * Stop listening for the current color changes
     */
    fun removeListener(listener: ColorListener)

}
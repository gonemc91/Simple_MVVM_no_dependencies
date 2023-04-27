package com.example.simple_mvvm.model.colors

import com.example.foundation.model.Repository
import com.example.foundation.model.tasks.Task

typealias ColorListener = (NamedColor) -> Unit

/**
 * Repository interface example
 *
 * Provides access to the available colors and current selected colors
 */

interface ColorsRepository: Repository {

    /*var currentColor: NamedColor*/

    /**
     * Get the list of all available colors that that may be chosen by the user
     */
    fun getAvailableColors(): Task<List<NamedColor>>

    /**
     * Get the color content by its ID
     */
    fun getById(id: Long): Task<NamedColor>

    /**
     * Get the color content by its ID
     */
    fun getCurrentColor(): Task<NamedColor>

    /**
     * Set the specified color as current.
     */
    fun setCurrentColor(color: NamedColor): Task<Unit>

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
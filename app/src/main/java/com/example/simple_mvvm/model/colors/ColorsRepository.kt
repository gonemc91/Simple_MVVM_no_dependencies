package com.example.simple_mvvm.model.colors

import com.example.foundation.model.Repository
import kotlinx.coroutines.flow.Flow

typealias ColorListener = (NamedColor) -> Unit

/**
 * Repository interface example
 *
 * Provides access to the available colors and current selected colors
 */

interface ColorsRepository: Repository {


    /**
     * Get the list of all available colors that that may be chosen by the user
     */
   suspend fun getAvailableColors(): List<NamedColor>

    /**
     * Get the color content by its ID
     */
   suspend fun getById(id: Long): NamedColor

    /**
     * Get the color content by its ID
     */
   suspend fun getCurrentColor():NamedColor

    /**
     * Set the specified color as current.
     */
    fun setCurrentColor(color: NamedColor): Flow<Int>

    fun listenCurrentListener(): Flow<NamedColor>



}
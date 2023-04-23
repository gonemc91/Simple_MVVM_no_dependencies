package com.example.simple_mvvm.views.changecolor

import com.example.simple_mvvm.model.colors.NamedColor

/**
 * Represents list item for the color; it may be selected or not
 *
 */
data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)
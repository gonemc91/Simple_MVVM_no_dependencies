package com.example.foundation.views

import android.os.Parcelable


interface BaseScreen: Parcelable {

    /**
     * Base class for defining screen arguments.
     * Please note that all fields inside the screen should be serializable
     */

    companion object {
        const val ARG_SCREEN = "ARG_SCREEN"
    }


}
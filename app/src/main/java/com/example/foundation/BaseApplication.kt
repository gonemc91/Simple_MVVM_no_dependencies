package com.example.foundation

import com.example.foundation.model.Repository

/**
 * Implement this interface in your Application Class
 * Do not forget to add the application into the AndroidManifest.xml file.
 */

interface BaseApplication {

    /**
     * The list of repositories that can be added to the fragment view-model constructor.
     * The list of singleton scope dependencies can be added to the fragment
     * view model constructor.
     */

    val singletonScopeDependencies: List<Any>
}
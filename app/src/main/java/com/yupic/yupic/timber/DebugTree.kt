package com.yupic.yupic.timber

import timber.log.Timber

/**
 * Timber tree using in the debug variant.
 *
 * For this tree we are not sending the fails in the debug mode to Firebase Crashlytics
 */
open class DebugTree : Timber.DebugTree() {


    /**
     * Custom the message that appears in the console.
     * Example:
     * `AppName:187: Timber set up in DEBUG level`
     */
    override fun createStackElementTag(element: StackTraceElement): String? {
        return super.createStackElementTag(element) + ':' + element.lineNumber
    }

    /** Return whether a message at `priority` or `tag` should be logged.  */
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return true // In debug mode we log everything
    }
}
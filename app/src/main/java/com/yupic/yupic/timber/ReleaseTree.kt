package com.yupic.yupic.timber

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber


/**
 * Timber tree using in the release variant
 */
class ReleaseTree : Timber.Tree() {


    private val MAX_LOG_LENGTH = 4000


    /**
     * Return whether a message at `priority` or `tag` should be logged.
     * In this tree we just log warning and errors.
     */
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return (priority != Log.DEBUG && priority != Log.INFO && priority != Log.VERBOSE && priority != Log.ASSERT)
    }


    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     */
    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isLoggable(tag, priority)) {

            // Report caught exception to your crash library
            FirebaseCrashlytics.getInstance().log(tag + message + t?.message)

            // If message is short enough
            if (message.length < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, message)
                } else {
                    Log.println(priority, tag, message)
                }
                return
            }

            // If the message is not short enough we need to split into chunks
            // Split by line, then ensure each line can fit  into Log's  maximum length
            var i = 0
            val length = message.length
            while (i < length) {
                var newLine = message.indexOf('\n', i)
                newLine = if (newLine != -1) newLine else length
                do {
                    val end = Math.min(newLine, i + MAX_LOG_LENGTH)
                    val part = message.substring(i, end)
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, part)
                    } else {
                        Log.println(priority, tag, part)
                    }
                    i = end
                } while (i < newLine)
                i++
            }
        }
    }


}
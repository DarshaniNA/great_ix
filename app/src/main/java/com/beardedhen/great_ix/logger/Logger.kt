package com.beardedhen.great_ix.logger

import android.util.Log
import com.beardedhen.great_ix.BuildConfig

/**
 * Simple logging utils
 */
class Logger {

    companion object {
        private val TAG = BuildConfig.APPLICATION_ID

        fun v(log: String) {
            Log.v(TAG, log)
        }
        fun v(log: String, t: Throwable) {
            Log.v(TAG, log, t)
        }

        fun d(log: String) {
            Log.d(TAG, log)
        }
        fun d(log: String, t: Throwable) {
            Log.d(TAG, log, t)
        }

        fun i(log: String) {
            Log.i(TAG, log)
        }
        fun i(log: String, t: Throwable) {
            Log.i(TAG, log, t)
        }

        fun w(log: String) {
            Log.w(TAG, log)
        }
        fun w(log: String, t: Throwable) {
            Log.w(TAG, log, t)
        }

        fun e(log: String) {
            Log.e(TAG, log)
        }
        fun e(log: String, t: Throwable) {
            Log.e(TAG, log, t)
        }
    }
}
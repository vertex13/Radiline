package xyz.skether.radiline

import android.util.Log

fun Any.logInfo(message: String) {
    Log.i(javaClass.simpleName, message)
}

fun Any.logError(message: String) {
    Log.e(javaClass.simpleName, message)
}

fun Any.logWarn(message: String) {
    Log.w(javaClass.simpleName, message)
}


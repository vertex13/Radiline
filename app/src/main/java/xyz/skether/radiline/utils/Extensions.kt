package xyz.skether.radiline.utils

import android.util.Log
import androidx.lifecycle.MutableLiveData

fun Any.logInfo(message: String) {
    Log.i(javaClass.simpleName, message)
}

fun Any.logError(message: String) {
    Log.e(javaClass.simpleName, message)
}

fun Any.logError(throwable: Throwable) {
    Log.e(javaClass.simpleName, throwable.toString())
}

fun Any.logError(message: String, throwable: Throwable) {
    Log.e(javaClass.simpleName, "$message\n$throwable")
}

fun Any.logWarn(message: String) {
    Log.w(javaClass.simpleName, message)
}

/**
 * Notify observers that the value has been updated.
 */
fun <T> MutableLiveData<T>.notify() {
    value = value
}

fun MutableLiveData<Throwable?>.setError(error: Throwable) {
    value = error
    value = null
}

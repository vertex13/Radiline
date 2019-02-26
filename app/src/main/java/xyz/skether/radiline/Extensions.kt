package xyz.skether.radiline

import android.util.Log
import androidx.lifecycle.MutableLiveData

fun Any.logInfo(message: String) {
    Log.i(javaClass.simpleName, message)
}

fun Any.logError(message: String) {
    Log.e(javaClass.simpleName, message)
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

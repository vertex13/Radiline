package xyz.skether.radiline.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SuspendLazy<T>(private val func: suspend (() -> T)) {

    private lateinit var valueContainer: Container<T>
    private val mutex = Mutex()

    suspend fun getValue(): T {
        if (!this::valueContainer.isInitialized) {
            mutex.withLock {
                if (!this::valueContainer.isInitialized) {
                    valueContainer = Container(func())
                }
            }
        }
        return valueContainer.value
    }

    private class Container<T>(val value: T)

}

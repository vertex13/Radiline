package xyz.skether.radiline.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LayoutId(@LayoutRes val id: Int)

class NoLayoutIdException(message: String) : RuntimeException(message)

/**
 * @throws [NoLayoutIdException] if the [clazz] does not have a [LayoutId] annotation
 */
@Throws(NoLayoutIdException::class)
fun <T> getLayoutIdAnnotation(clazz: Class<T>): LayoutId {
    return clazz.annotations.find { it is LayoutId } as LayoutId?
        ?: throw NoLayoutIdException("${clazz.simpleName} does not have a @LayoutId annotation.")
}

inline fun <reified T> newViewHolder(parent: ViewGroup): T {
    val layoutAnnotation = getLayoutIdAnnotation(T::class.java)
    val view = LayoutInflater.from(parent.context).inflate(layoutAnnotation.id, parent, false)
    return T::class.java.getConstructor(View::class.java).newInstance(view)
}

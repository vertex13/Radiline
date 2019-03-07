package xyz.skether.radiline.ui.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

    private companion object {
        const val TIME_BETWEEN_SNACKBARS = 1000L // 1 second
    }

    private var snackbarLastTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutAnnotation = getLayoutIdAnnotation(this.javaClass)
        setContentView(layoutAnnotation.id)
    }

    fun showSnackbar(@StringRes textId: Int, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(window.decorView.rootView, textId, length).show()
        snackbarLastTime = System.currentTimeMillis()
    }

    fun showSnackbarAllowingSkip(@StringRes textId: Int, length: Int = Snackbar.LENGTH_LONG) {
        if (System.currentTimeMillis() > snackbarLastTime + TIME_BETWEEN_SNACKBARS) {
            showSnackbar(textId, length)
        }
    }

}

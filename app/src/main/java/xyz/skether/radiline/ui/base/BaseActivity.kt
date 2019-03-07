package xyz.skether.radiline.ui.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutAnnotation = getLayoutIdAnnotation(this.javaClass)
        setContentView(layoutAnnotation.id)
    }

    fun showSnackbar(@StringRes textId: Int, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(window.decorView.rootView, textId, length).show()
    }

}

package xyz.skether.radiline.ui.base

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(view: View, textId: Int, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, textId, length).show()
}

fun showSnackbar(view: View, @StringRes text: String, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, text, length).show()
}
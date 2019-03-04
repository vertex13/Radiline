package xyz.skether.radiline.ui.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(view: View, @StringRes textId: Int, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, textId, length).show()
}

fun showSnackbar(view: View, text: String, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, text, length).show()
}

fun hideKeyboard(focusedView: View) {
    val imm = focusedView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
}

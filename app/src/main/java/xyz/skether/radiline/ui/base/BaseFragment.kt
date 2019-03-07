package xyz.skether.radiline.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutAnnotation = getLayoutIdAnnotation(this.javaClass)
        return inflater.inflate(layoutAnnotation.id, container, false)
    }

    fun showSnackbar(@StringRes textId: Int, length: Int = Snackbar.LENGTH_LONG) {
        (activity as? BaseActivity)?.showSnackbar(textId, length)
    }

    fun showSnackbarAllowingSkip(@StringRes textId: Int, length: Int = Snackbar.LENGTH_LONG) {
        (activity as? BaseActivity)?.showSnackbarAllowingSkip(textId, length)
    }

}

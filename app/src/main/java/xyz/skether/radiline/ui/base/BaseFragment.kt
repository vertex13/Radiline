package xyz.skether.radiline.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutAnnotation = getLayoutIdAnnotation(this.javaClass)
        return inflater.inflate(layoutAnnotation.id, container, false)
    }

}

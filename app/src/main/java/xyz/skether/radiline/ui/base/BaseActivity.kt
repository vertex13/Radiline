package xyz.skether.radiline.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutAnnotation = getLayoutIdAnnotation(this.javaClass)
        setContentView(layoutAnnotation.id)
    }

}

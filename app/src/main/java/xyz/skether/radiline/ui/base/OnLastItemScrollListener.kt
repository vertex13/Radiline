package xyz.skether.radiline.ui.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OnLastItemScrollListener(
    /**
     * Should be >= 1.
     */
    private val numberOfLastItems: Int = 1,
    /**
     * Invoked when the last items reached.
     */
    private val callback: () -> Unit
) : RecyclerView.OnScrollListener() {

    init {
        if (numberOfLastItems < 1) {
            throw IllegalArgumentException("numberOfLastItems should be >= 1.")
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val lm = recyclerView.layoutManager!! as LinearLayoutManager
        val totalItemCount = lm.itemCount
        val lastVisibleItemPosition = lm.findLastVisibleItemPosition()

        if (lastVisibleItemPosition >= 0
            && (lastVisibleItemPosition + numberOfLastItems >= totalItemCount)
        ) {
            callback.invoke()
        }
    }

}

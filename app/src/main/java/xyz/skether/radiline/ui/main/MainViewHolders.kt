package xyz.skether.radiline.ui.main

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vh_main_genre.view.*
import kotlinx.android.synthetic.main.vh_main_search.view.*
import kotlinx.android.synthetic.main.vh_main_station.view.*
import xyz.skether.radiline.R
import xyz.skether.radiline.ui.base.LayoutId
import xyz.skether.radiline.ui.base.hideKeyboard

@LayoutId(R.layout.vh_main_station)
class StationMainVH(view: View) : RecyclerView.ViewHolder(view) {

    fun init(item: StationMainItem, onClickListener: (StationMainItem) -> Unit) {
        val bgColorId = if (item.subLevel == 0) 0 else R.color.black_8p
        itemView.setBackgroundResource(bgColorId)
        itemView.setOnClickListener { onClickListener(item) }

        val leftPadding = itemView.resources.getDimensionPixelSize(R.dimen.space_normal) * item.subLevel
        itemView.vhmst_name.apply {
            text = item.station.name
            setPadding(leftPadding, 0, 0, 0)
        }
        itemView.vhmst_listeners_label.setPadding(leftPadding, 0, 0, 0)
        itemView.vhmst_listeners.text = item.station.listeners.toString()
        itemView.vhmst_bitrate.text = item.station.bitrate.toString()
    }

}

@LayoutId(R.layout.vh_main_genre)
class GenreMainVH(view: View) : RecyclerView.ViewHolder(view) {

    fun init(item: GenreMainItem, onClickListener: (GenreMainItem) -> Unit) {
        val leftPadding = itemView.resources.getDimensionPixelSize(R.dimen.space_normal) * item.subLevel
        itemView.setOnClickListener { onClickListener(item) }
        itemView.vhmg_name.apply {
            text = item.genre.name
            setPadding(leftPadding, 0, 0, 0)
        }
    }

}

@LayoutId(R.layout.vh_main_search)
class SearchMainVH(view: View) : RecyclerView.ViewHolder(view) {

    private val handler = Handler()
    private var onQueryChanged: ((String) -> Unit)? = null
    private val typingDelay = 2000L // 2 seconds delay
    private val afterTypingDelay = Runnable {
        onQueryChanged?.invoke(itemView.vhmsr_query.text.toString())
    }
    private var textWatcher: TextWatcher? = null

    init {
        itemView.vhmsr_query.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handler.removeCallbacks(afterTypingDelay)
                hideKeyboard(textView)
                onQueryChanged?.invoke(textView.text.toString())
                true
            } else {
                false
            }
        }
    }

    fun init(item: SearchMainItem, onQueryChanged: (query: String) -> Unit) {
        this.onQueryChanged = onQueryChanged
        itemView.vhmsr_query.apply {
            removeTextChangedListener(textWatcher)
            setText(item.query)
            textWatcher = SearchTextWatcher(item)
            addTextChangedListener(textWatcher)
        }
    }

    private inner class SearchTextWatcher(val item: SearchMainItem) : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            item.query = editable.toString()
            handler.removeCallbacks(afterTypingDelay)
            if (editable.isNotEmpty()) {
                handler.postDelayed(afterTypingDelay, typingDelay)
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

}

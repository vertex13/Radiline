package xyz.skether.radiline.ui.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vh_main_genre.view.*
import kotlinx.android.synthetic.main.vh_main_search.view.*
import kotlinx.android.synthetic.main.vh_main_station.view.*
import xyz.skether.radiline.R
import xyz.skether.radiline.ui.base.LayoutId

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

    fun init(item: SearchMainItem, onQueryListener: (query: String) -> Unit) {
        itemView.vhmsr_query.setText(item.query)
    }

}

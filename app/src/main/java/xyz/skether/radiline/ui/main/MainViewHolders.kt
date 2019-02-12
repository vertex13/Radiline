package xyz.skether.radiline.ui.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vh_main_genre.view.*
import kotlinx.android.synthetic.main.vh_main_station.view.*
import xyz.skether.radiline.R
import xyz.skether.radiline.ui.base.LayoutId

@LayoutId(R.layout.vh_main_station)
class StationMainVH(view: View) : RecyclerView.ViewHolder(view) {

    fun init(item: StationMainItem, onClickListener: (StationMainItem) -> Unit) {
        itemView.vhmst_name.apply {
            text = item.station.name
            setOnClickListener { onClickListener(item) }
        }
    }

}

@LayoutId(R.layout.vh_main_genre)
class GenreMainVH(view: View) : RecyclerView.ViewHolder(view) {

    fun init(item: GenreMainItem, onClickListener: (GenreMainItem) -> Unit) {
        itemView.vhmg_name.apply {
            text = item.genre.name
            setOnClickListener { onClickListener(item) }
        }
    }

}

@LayoutId(R.layout.vh_main_search)
class SearchMainVH(view: View) : RecyclerView.ViewHolder(view) {

    fun init(item: SearchMainItem, onQueryListener: (query: String) -> Unit) {
        // todo
    }

}

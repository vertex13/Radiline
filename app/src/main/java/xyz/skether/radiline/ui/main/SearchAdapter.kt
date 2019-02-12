package xyz.skether.radiline.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.newViewHolder

class SearchAdapter(private val stations: List<Station>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<MainItem>()

    init {
        items.add(SearchMainItem())
        items.addAll(stations.map { StationMainItem(it) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderType = MainItem.Type.values()[viewType]
        return when (holderType) {
            MainItem.Type.SEARCH -> newViewHolder<SearchMainVH>(parent)
            MainItem.Type.STATION -> newViewHolder<StationMainVH>(parent)
            else -> throw IllegalStateException("The $holderType view holder is not supported.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is SearchMainItem -> {
                holder as SearchMainVH
                holder.init(item, this::onQuery)
            }
            is StationMainItem -> {
                holder as StationMainVH
                holder.init(item, this::onStationClicked)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    fun getItem(position: Int): MainItem = items[position]

    private fun onQuery(query: String) {
        // todo
    }

    private fun onStationClicked(stationItem: StationMainItem) {
        // todo play the station
    }

}

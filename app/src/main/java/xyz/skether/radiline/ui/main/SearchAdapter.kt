package xyz.skether.radiline.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.newViewHolder

class SearchAdapter(private val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<MainItem>()

    init {
        items.add(SearchMainItem())
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

    fun updateData(stations: List<Station>) {
        val searchItem = items.first() as SearchMainItem
        items.clear()
        items.add(searchItem)
        items.addAll(stations.map { StationMainItem(it) })
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): MainItem = items[position]

    private fun onQuery(query: String) {
        callback.onQuery(query)
    }

    private fun onStationClicked(item: StationMainItem) {
        callback.onStationSelected(item.station)
    }

    interface Callback {

        fun onQuery(query: String)

        fun onStationSelected(station: Station)

    }

}

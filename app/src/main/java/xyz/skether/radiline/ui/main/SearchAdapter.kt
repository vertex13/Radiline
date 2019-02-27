package xyz.skether.radiline.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.newViewHolder
import kotlin.math.min

class SearchAdapter(private val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object Config {
        const val HEADER_ITEMS_SIZE = 1 // Search item
    }

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
                holder.init(item, this::onQueryChanged)
            }
            is StationMainItem -> {
                holder as StationMainVH
                holder.init(item, this::onStationClicked)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    fun updateData(updatedStations: List<Station>) {
        val stationItems = items.subList(HEADER_ITEMS_SIZE, items.size)
        var inconsistencyPos = min(stationItems.size, updatedStations.size)
        for (i in 0 until inconsistencyPos) {
            val item = stationItems[i] as StationMainItem
            val station = updatedStations[i]
            if (item.station != station) {
                inconsistencyPos = i
                break
            }
        }
        inconsistencyPos += HEADER_ITEMS_SIZE

        if (inconsistencyPos < items.size) {
            // Remove old items.
            val itemsToRemove = items.subList(inconsistencyPos, items.size)
            val itemsToRemoveSize = itemsToRemove.size
            itemsToRemove.clear()
            notifyItemRangeRemoved(inconsistencyPos, itemsToRemoveSize)
        }

        val newStationsFrom = inconsistencyPos - HEADER_ITEMS_SIZE
        if (newStationsFrom < updatedStations.size) {
            // Insert new stations.
            val newStations = updatedStations.subList(newStationsFrom, updatedStations.size)
            items.addAll(newStations.map { StationMainItem(it) })
            notifyItemRangeInserted(inconsistencyPos, newStations.size)
        }
    }

    private fun getItem(position: Int): MainItem = items[position]

    private fun onQueryChanged(query: String) {
        callback.onQueryChanged(query)
    }

    private fun onStationClicked(item: StationMainItem) {
        callback.onStationSelected(item.station)
    }

    interface Callback {

        fun onQueryChanged(query: String)

        fun onStationSelected(station: Station)

    }

}

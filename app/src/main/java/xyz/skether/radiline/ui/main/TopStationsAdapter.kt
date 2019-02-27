package xyz.skether.radiline.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.newViewHolder
import kotlin.math.min

class TopStationsAdapter(private val callback: Callback) : RecyclerView.Adapter<StationMainVH>() {

    private val items = mutableListOf<StationMainItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationMainVH {
        return newViewHolder(parent)
    }

    override fun onBindViewHolder(holder: StationMainVH, position: Int) {
        holder.init(getItem(position), this::onStationClicked)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(updatedStations: List<Station>) {
        var inconsistencyPos = min(items.size, updatedStations.size)
        for (i in 0 until inconsistencyPos) {
            val item = items[i]
            val station = updatedStations[i]
            if (item.station != station) {
                inconsistencyPos = i
                break
            }
        }

        if (inconsistencyPos < items.size) {
            // Remove old items.
            val itemsToRemove = items.subList(inconsistencyPos, items.size)
            val itemsToRemoveSize = itemsToRemove.size
            itemsToRemove.clear()
            notifyItemRangeRemoved(inconsistencyPos, itemsToRemoveSize)
        }

        if (inconsistencyPos < updatedStations.size) {
            // Insert new stations.
            val newStations = updatedStations.subList(inconsistencyPos, updatedStations.size)
            items.addAll(newStations.map { StationMainItem(it) })
            notifyItemRangeInserted(inconsistencyPos, newStations.size)
        }
    }

    private fun getItem(position: Int): StationMainItem = items[position]

    private fun onStationClicked(item: StationMainItem) {
        callback.onStationSelected(item.station)
    }

    interface Callback {

        fun onStationSelected(station: Station)

    }

}

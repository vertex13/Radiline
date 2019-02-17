package xyz.skether.radiline.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.newViewHolder

class TopStationsAdapter(private val callback: Callback) : RecyclerView.Adapter<StationMainVH>() {

    private val items = mutableListOf<StationMainItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationMainVH {
        return newViewHolder(parent)
    }

    override fun onBindViewHolder(holder: StationMainVH, position: Int) {
        holder.init(getItem(position), this::onStationClicked)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(stations: List<Station>) {
        items.clear()
        items.addAll(stations.map { StationMainItem(it) })
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): StationMainItem = items[position]

    private fun onStationClicked(item: StationMainItem) {
        callback.onStationSelected(item.station)
    }

    interface Callback {

        fun onStationSelected(station: Station)

    }

}

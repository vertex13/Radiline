package xyz.skether.radiline.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.newViewHolder

class TopStationsAdapter(private val stations: List<Station>): RecyclerView.Adapter<StationMainVH>() {

    private val items: List<StationMainItem> = stations.map { StationMainItem(it) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationMainVH {
        return newViewHolder(parent)
    }

    override fun onBindViewHolder(holder: StationMainVH, position: Int) {
        holder.init(getItem(position), this::onStationClicked)
    }

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int): StationMainItem = items[position]

    private fun onStationClicked(item: StationMainItem) {
        // todo play the station
    }

}

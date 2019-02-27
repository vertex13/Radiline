package xyz.skether.radiline.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.newViewHolder
import kotlin.math.min

class GenresAdapter(private val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<MainItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderType = MainItem.Type.values()[viewType]
        return when (holderType) {
            MainItem.Type.STATION -> newViewHolder<StationMainVH>(parent)
            MainItem.Type.GENRE -> newViewHolder<GenreMainVH>(parent)
            else -> throw IllegalStateException("The $holderType view holder is not supported.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is StationMainItem -> {
                holder as StationMainVH
                holder.init(item, this::onStationClicked)
            }
            is GenreMainItem -> {
                holder as GenreMainVH
                holder.init(item, this::onGenreClicked)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    fun setData(newItems: List<MainItem>) {
        val oldSize = items.size
        items.clear()
        notifyItemRangeRemoved(0, oldSize)
        items.addAll(newItems)
        notifyItemRangeInserted(0, newItems.size)
    }

    fun updateData(updatedItems: List<MainItem>) {
        var inconsistencyPos = min(items.size, updatedItems.size)
        for (i in 0 until inconsistencyPos) {
            val item = items[i]
            val updatedItem = updatedItems[i]
            if (item != updatedItem) {
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

        if (inconsistencyPos < updatedItems.size) {
            // Insert new items.
            val newItems = updatedItems.subList(inconsistencyPos, updatedItems.size)
            items.addAll(newItems)
            notifyItemRangeInserted(inconsistencyPos, newItems.size)
        }
    }

    private fun getItem(position: Int) = items[position]

    private fun onStationClicked(item: StationMainItem) {
        callback.onStationSelected(item.station)
    }

    private fun onGenreClicked(item: GenreMainItem) {
        callback.onGenreSelected(item.genre)
    }

    interface Callback {

        fun onGenreSelected(genre: Genre)

        fun onStationSelected(station: Station)

    }

}

package xyz.skether.radiline.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.ui.base.newViewHolder

class GenresAdapter(private val genres: List<Genre>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<MainItem>()
    private val expandedGenres = mutableListOf<GenreMainItem>()

    init {
        items.addAll(buildItemList())
    }

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

    fun getItem(position: Int) = items[position]

    private fun onStationClicked(stationItem: StationMainItem) {
        // todo play the station
    }

    private fun onGenreClicked(item: GenreMainItem) {
        if (isGenreExpanded(item)) {
            closeGenre(item)
        } else {
            openGenre(item)
        }
    }

    private fun isGenreExpanded(genreItem: GenreMainItem): Boolean {
        return expandedGenres.find { sameGenreItem(it, genreItem) } != null
    }

    private fun sameGenreItem(item1: GenreMainItem, item2: GenreMainItem): Boolean {
        return item1.genre.id == item2.genre.id
    }

    private fun openGenre(item: GenreMainItem) {
        if (expandedGenres.size > item.subLevel) {
            closeGenre(expandedGenres[item.subLevel])
        }

        expandedGenres.add(item)
        val insertedItems = buildItemList(item.subLevel.inc(), item.genre)
        val insertFrom = items.indexOf(item).inc()

        items.addAll(insertFrom, insertedItems)
        notifyItemRangeInserted(insertFrom, insertedItems.size)
    }

    private fun closeGenre(item: GenreMainItem) {
        var numClosedItems = 0
        for (i in item.subLevel until expandedGenres.size) {
            numClosedItems += expandedGenres[i].innerItemsSize()
        }
        val closeFrom = items.indexOf(item).inc()
        val closeTo = closeFrom + numClosedItems

        expandedGenres.subList(item.subLevel, expandedGenres.size).clear()
        items.subList(closeFrom, closeTo).clear()
        notifyItemRangeRemoved(closeFrom, numClosedItems)
    }

    private fun buildItemList(
        subLevel: Int = 0,
        expandedGenre: Genre? = null
    ): List<MainItem> {
        val newItems = mutableListOf<MainItem>()

        val subGenres = when {
            expandedGenre == null -> genres
            expandedGenre.hasSubGenres -> when {
                expandedGenre.subGenres != null -> expandedGenre.subGenres!!
                else -> {
                    // todo load sub genres
                    emptyList()
                }
            }
            else -> emptyList()
        }

        val hasExpanded = expandedGenres.size > subLevel
        for (genre in subGenres) {
            newItems.add(GenreMainItem(genre, subLevel))
            if (hasExpanded && genre.id == expandedGenres[subLevel].genre.id) {
                newItems.addAll(buildItemList(subLevel.inc(), genre))
            }
        }

        if (expandedGenre != null) {
            if (expandedGenre.stations != null) {
                newItems.addAll(expandedGenre.stations!!.map { StationMainItem(it, subLevel) })
            } else {
                // todo load stations
            }
        }

        return newItems
    }

}

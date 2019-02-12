package xyz.skether.radiline.ui.main

import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.Station

sealed class MainItem(val type: Type) {
    enum class Type { STATION, GENRE, SEARCH }
}

class StationMainItem(var station: Station, var subLevel: Int = 0) : MainItem(Type.STATION)

class GenreMainItem(var genre: Genre, var subLevel: Int = 0) : MainItem(Type.GENRE) {

    fun innerItemsSize() = (genre.subGenres?.size ?: 0) + (genre.stations?.size ?: 0)

}

class SearchMainItem(var query: String = "") : MainItem(Type.SEARCH)

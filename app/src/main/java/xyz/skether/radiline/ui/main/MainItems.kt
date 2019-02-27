package xyz.skether.radiline.ui.main

import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.Station

sealed class MainItem(val type: Type) {
    enum class Type { STATION, GENRE, SEARCH }
}

data class StationMainItem(var station: Station) : MainItem(Type.STATION)

data class GenreMainItem(var genre: Genre) : MainItem(Type.GENRE)

data class SearchMainItem(var query: String = "") : MainItem(Type.SEARCH)

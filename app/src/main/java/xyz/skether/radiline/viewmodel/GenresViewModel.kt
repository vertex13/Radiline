package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.Station

class GenresViewModel : BaseViewModel() {

    private val _genres = MutableLiveData<List<Genre>>()

    val genres: LiveData<List<Genre>>
        get() = _genres

    init {
        loadGenres()
    }

    fun loadSubGenres(genre: Genre) {
        if (!genre.hasSubGenres || genre.subGenres != null) {
            return
        }
        // todo
    }

    fun loadStations(genre: Genre) {
        if (genre.areAllStationsLoaded) {
            return
        }
        // todo
    }

    private fun loadGenres() {
        launch(Dispatchers.Default) {
            _genres.postValue(mockGenres())
        }
    }

    private suspend fun mockGenres(): List<Genre> {
        delay(1000)
        val genres = mutableListOf<Genre>()
        for (i in 1..10) {
            val genre = Genre(i, "Genre #$i", true)
            genre.subGenres = mutableListOf()
            for (j in 1..3) {
                val subGenre = Genre(i * 1000 + j, "Sub Genre #$j", false, genre)
                subGenre.stations = mutableListOf()
                for (k in 1..5) {
                    subGenre.stations!!.add(Station(i, "Station #$k - Genre $i-$j", genre = subGenre))
                }
                genre.subGenres!!.add(subGenre)
            }
            genre.stations = mutableListOf()
            for (k in 1..2) {
                genre.stations!!.add(Station(i, "Station #$k - Genre $i", genre = genre))
            }
            genres.add(genre)
        }
        return genres
    }

}

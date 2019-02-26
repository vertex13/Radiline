package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.GenresManager
import xyz.skether.radiline.domain.StationsManager
import xyz.skether.radiline.domain.di.Injector
import javax.inject.Inject

class GenresViewModel : BaseViewModel() {

    private companion object Config {
        const val PAGE_SIZE = 10
    }

    @Inject
    lateinit var genresManager: GenresManager
    @Inject
    lateinit var stationManager: StationsManager

    private val _genres: MutableLiveData<List<Genre>> by lazy {
        MutableLiveData<List<Genre>>().also {
            loadGenres()
        }
    }

    val genres: LiveData<List<Genre>>
        get() = _genres

    private val currentTasks = mutableSetOf<String>()

    init {
        Injector.appComponent.inject(this)
    }

    fun loadSubGenres(genre: Genre) {
        val key = "load_sub_genres_for_${genre.id}"
        if (!genre.hasSubGenres || genre.subGenres != null || currentTasks.contains(key)) {
            return
        }
        launch {
            currentTasks.add(key)
            val subGenres = withContext(Dispatchers.Default) {
                genresManager.getGenres(genre.id)
            }
            genre.subGenres = mutableListOf()
            for (subGenre in subGenres) {
                subGenre.parent = genre
                genre.subGenres!!.add(subGenre)
            }
            _genres.value = _genres.value
            currentTasks.remove(key)
        }
    }

    fun loadStations(genre: Genre) {
        val key = "load_stations_for_${genre.id}"
        if (genre.areAllStationsLoaded || currentTasks.contains(key)) {
            return
        }
        launch {
            currentTasks.add(key)
            val offset = genre.stations?.size ?: 0
            val stations = withContext(Dispatchers.Default) {
                stationManager.getStationsByGenreId(genre.id, PAGE_SIZE, offset)
            }
            if (stations.size != PAGE_SIZE) {
                genre.areAllStationsLoaded = true
            }
            if (genre.stations == null) {
                genre.stations = mutableListOf()
            }
            for (station in stations) {
                station.genre = genre
                genre.stations!!.add(station)
            }
            _genres.value = _genres.value
            currentTasks.remove(key)
        }
    }

    private fun loadGenres() {
        launch(Dispatchers.Default) {
            _genres.postValue(genresManager.getGenres())
        }
    }

}

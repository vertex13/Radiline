package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.skether.radiline.data.shoutcast.ShoutcastError
import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.di.Injector
import xyz.skether.radiline.domain.manager.GenresManager
import xyz.skether.radiline.domain.manager.StationsManager
import xyz.skether.radiline.utils.notify
import xyz.skether.radiline.utils.setError
import javax.inject.Inject

class GenresViewModel : BaseViewModel() {

    private companion object Config {
        const val PAGE_SIZE = 10
    }

    @Inject
    lateinit var genresManager: GenresManager
    @Inject
    lateinit var stationManager: StationsManager

    private val _genres: MutableLiveData<MutableList<Genre>> by lazy {
        MutableLiveData<MutableList<Genre>>().also {
            it.value = mutableListOf()
            loadGenres()
        }
    }

    val genres: LiveData<out List<Genre>>
        get() = _genres

    private val _error = MutableLiveData<Throwable?>()

    val error: LiveData<Throwable?>
        get() = _error

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
            try {
                val subGenres = withContext(Dispatchers.Default) {
                    genresManager.getSecondaryGenres(genre.id)
                }
                genre.subGenres = mutableListOf()
                for (subGenre in subGenres) {
                    subGenre.parent = genre
                    genre.subGenres!!.add(subGenre)
                }
                _genres.notify()
            } catch (e: ShoutcastError) {
                _error.setError(e)
            }
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
            try {
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
                _genres.notify()
            } catch (e: ShoutcastError) {
                _error.setError(e)
            }
            currentTasks.remove(key)
        }
    }

    private fun loadGenres() {
        launch {
            try {
                val primaryGenres = withContext(Dispatchers.Default) { genresManager.getPrimaryGenres() }
                _genres.value!!.addAll(primaryGenres)
                _genres.notify()
            } catch (e: ShoutcastError) {
                _error.setError(e)
            }
        }
    }

}

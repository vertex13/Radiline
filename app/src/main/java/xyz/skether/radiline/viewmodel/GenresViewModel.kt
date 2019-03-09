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
            loadPrimaryGenres()
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

    fun loadStations(genre: Genre, forceUpdate: Boolean = false) {
        val tag = "load_stations_for_${genre.id}"
        if (genre.areAllStationsLoaded && !forceUpdate) {
            return
        }
        launchRequest(tag) {
            val offset = if (forceUpdate) 0 else genre.stations?.size ?: 0
            val stations = withContext(Dispatchers.Default) {
                stationManager.getStationsByGenreId(genre.id, PAGE_SIZE, offset, forceUpdate)
            }
            if (forceUpdate) {
                genre.areAllStationsLoaded = false
            }
            if (stations.size != PAGE_SIZE) {
                genre.areAllStationsLoaded = true
            }
            if (genre.stations == null || forceUpdate) {
                genre.stations = mutableListOf()
            }
            for (station in stations) {
                station.genre = genre
                genre.stations!!.add(station)
            }
            _genres.notify()
        }
    }

    fun loadGenres(parentGenre: Genre? = null, forceUpdate: Boolean = false) {
        if (parentGenre == null) {
            loadPrimaryGenres(forceUpdate)
        } else {
            loadSubGenres(parentGenre, forceUpdate)
        }
    }

    private fun loadPrimaryGenres(forceUpdate: Boolean = false) {
        launchRequest("load_primary_genres") {
            val primaryGenres = withContext(Dispatchers.Default) { genresManager.getPrimaryGenres(forceUpdate) }
            _genres.value!!.clear()
            _genres.value!!.addAll(primaryGenres)
            _genres.notify()
        }
    }

    private fun loadSubGenres(genre: Genre, forceUpdate: Boolean = false) {
        val tag = "load_sub_genres_for_${genre.id}"
        if (!genre.hasSubGenres
            || (genre.subGenres != null && !forceUpdate)
        ) {
            return
        }

        launchRequest(tag) {
            val subGenres = withContext(Dispatchers.Default) {
                genresManager.getSecondaryGenres(genre.id, forceUpdate)
            }
            genre.subGenres = mutableListOf()
            for (subGenre in subGenres) {
                subGenre.parent = genre
                genre.subGenres!!.add(subGenre)
            }
            _genres.notify()
        }
    }

    private fun launchRequest(tag: String, request: suspend (() -> Unit)) {
        if (currentTasks.contains(tag)) {
            return
        }
        launch {
            currentTasks.add(tag)
            try {
                request()
            } catch (e: ShoutcastError) {
                _error.setError(e)
            }
            currentTasks.remove(tag)
        }
    }

}

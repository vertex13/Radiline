package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.GenresManager
import xyz.skether.radiline.domain.di.Injector
import javax.inject.Inject

class GenresViewModel : BaseViewModel() {

    @Inject
    lateinit var genresManager: GenresManager

    private val _genres: MutableLiveData<List<Genre>> by lazy {
        MutableLiveData<List<Genre>>().also {
            loadGenres()
        }
    }

    val genres: LiveData<List<Genre>>
        get() = _genres

    init {
        Injector.appComponent.inject(this)
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
            _genres.postValue(genresManager.getPrimaryGenres())
        }
    }

}

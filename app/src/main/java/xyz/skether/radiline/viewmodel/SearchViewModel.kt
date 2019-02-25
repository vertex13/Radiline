package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationsManager
import xyz.skether.radiline.domain.di.Injector
import javax.inject.Inject

class SearchViewModel : BaseViewModel() {

    private companion object Config {
        const val MIN_QUERY_LENGTH = 3
    }

    @Inject
    lateinit var stationsManager: StationsManager

    private val _stations: MutableLiveData<List<Station>> by lazy {
        MutableLiveData<List<Station>>()
    }

    val stations: LiveData<List<Station>>
        get() = _stations

    init {
        Injector.appComponent.inject(this)
    }

    fun search(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.length < MIN_QUERY_LENGTH) {
            return
        }

        launch(Dispatchers.Default) {
            _stations.postValue(stationsManager.searchStations(query))
        }
    }

}

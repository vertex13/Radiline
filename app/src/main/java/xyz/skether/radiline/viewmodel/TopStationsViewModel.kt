package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationsManager
import xyz.skether.radiline.domain.di.Injector
import xyz.skether.radiline.notify
import javax.inject.Inject

class TopStationsViewModel : BaseViewModel() {

    private companion object Config {
        const val PAGE_SIZE = 20
    }

    @Inject
    lateinit var stationsManager: StationsManager

    private val _stations: MutableLiveData<MutableList<Station>> by lazy {
        MutableLiveData<MutableList<Station>>().also {
            it.value = mutableListOf()
            loadTopStations()
        }
    }

    val stations: LiveData<out List<Station>>
        get() = _stations

    private var isLoading = false

    init {
        Injector.appComponent.inject(this)
    }

    fun loadTopStations() {
        if (isLoading) {
            return
        }
        launch {
            isLoading = true
            val offset = _stations.value?.size ?: 0
            val newStations = withContext(Dispatchers.Default) {
                stationsManager.getTopStations(PAGE_SIZE, offset)
            }
            _stations.value?.addAll(newStations)
            _stations.notify()
            isLoading = false
        }
    }

}

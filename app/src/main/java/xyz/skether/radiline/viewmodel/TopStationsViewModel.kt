package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.skether.radiline.data.shoutcast.ShoutcastError
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.di.Injector
import xyz.skether.radiline.domain.manager.StationsManager
import xyz.skether.radiline.utils.notify
import xyz.skether.radiline.utils.setError
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

    private val _error = MutableLiveData<Throwable?>()

    val error: LiveData<Throwable?>
        get() = _error

    private var isLoading = false

    init {
        Injector.appComponent.inject(this)
    }

    fun loadTopStations(forceUpdate: Boolean = false) {
        if (isLoading) {
            return
        }
        launch {
            isLoading = true
            try {
                val offset = if(forceUpdate) 0 else _stations.value?.size ?: 0
                val newStations = withContext(Dispatchers.Default) {
                    stationsManager.getTopStations(PAGE_SIZE, offset, forceUpdate)
                }
                if (forceUpdate) {
                    _stations.value?.clear()
                }
                _stations.value?.addAll(newStations)
                _stations.notify()
            } catch (e: ShoutcastError) {
                _error.setError(e)
            }
            isLoading = false
        }
    }

}

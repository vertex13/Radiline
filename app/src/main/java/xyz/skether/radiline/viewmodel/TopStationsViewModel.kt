package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.StationsManager
import xyz.skether.radiline.domain.di.Injector
import javax.inject.Inject

class TopStationsViewModel : BaseViewModel() {

    @Inject
    lateinit var stationsManager: StationsManager

    private val _stations: MutableLiveData<List<Station>> by lazy {
        MutableLiveData<List<Station>>().also {
            loadTopStations()
        }
    }

    val stations: LiveData<List<Station>>
        get() = _stations

    init {
        Injector.appComponent.inject(this)
    }

    private fun loadTopStations() {
        launch(Dispatchers.Default) {
            _stations.postValue(stationsManager.getTopStations(10))
        }
    }

}

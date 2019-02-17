package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.skether.radiline.domain.Station

class TopStationsViewModel : BaseViewModel() {

    private val _stations = MutableLiveData<List<Station>>()

    val stations: LiveData<List<Station>>
        get() = _stations

    init {
        loadTopStations()
    }

    private fun loadTopStations() {
        launch(Dispatchers.Default) {
            _stations.postValue(mockStations())
        }
    }

    private suspend fun mockStations(): List<Station> {
        delay(1000)
        val stationList = mutableListOf<Station>()
        for (i in 1..50) {
            stationList.add(Station(i, "Station #$i"))
        }
        return stationList
    }

}

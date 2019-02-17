package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.skether.radiline.domain.Station

class SearchViewModel : BaseViewModel() {

    private val _stations = MutableLiveData<List<Station>>()

    val stations: LiveData<List<Station>>
        get() = _stations

    fun search(query: String) {
        launch(Dispatchers.Default) {
            _stations.postValue(mockStations(query))
        }
    }

    private suspend fun mockStations(query: String): List<Station> {
        delay(1000)
        val stationList = mutableListOf<Station>()
        for (i in 1..10) {
            stationList.add(Station(i, "$query station #$i"))
        }
        return stationList
    }

}

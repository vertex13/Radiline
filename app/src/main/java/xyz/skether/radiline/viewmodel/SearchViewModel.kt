package xyz.skether.radiline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.skether.radiline.data.shoutcast.ShoutcastError
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.domain.di.Injector
import xyz.skether.radiline.domain.manager.StationsManager
import xyz.skether.radiline.utils.notify
import xyz.skether.radiline.utils.setError
import javax.inject.Inject

class SearchViewModel : BaseViewModel() {

    private companion object Config {
        const val MIN_QUERY_LENGTH = 3
        const val PAGE_SIZE = 20
    }

    @Inject
    lateinit var stationsManager: StationsManager

    private val _stations: MutableLiveData<MutableList<Station>> by lazy {
        MutableLiveData<MutableList<Station>>().apply {
            value = mutableListOf()
        }
    }

    val stations: LiveData<out List<Station>>
        get() = _stations

    private val _error = MutableLiveData<Throwable?>()

    val error: LiveData<Throwable?>
        get() = _error

    private val state = SearchState()

    init {
        Injector.appComponent.inject(this)
    }

    fun search(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.length < MIN_QUERY_LENGTH) {
            return
        }

        state.reset(query)
        _stations.value?.clear()
        _stations.notify()

        val tag = "search"
        val job = launch {
            try {
                val newStations = withContext(Dispatchers.Default) {
                    stationsManager.searchStations(query, PAGE_SIZE)
                }
                _stations.value?.addAll(newStations)
                _stations.notify()
            } catch (e: ShoutcastError) {
                _error.setError(e)
            }
            state.jobMap.remove(tag)
        }
        state.jobMap[tag] = job
    }

    fun loadMore() {
        val tag = "load_more"
        val query = state.query
        if (query == null || state.jobMap.contains(tag) || state.isEndReached) {
            return
        }

        val job = launch {
            try {
                val offset = _stations.value?.size ?: 0
                val newStations = withContext(Dispatchers.Default) {
                    stationsManager.searchStations(query, PAGE_SIZE, offset)
                }
                if (newStations.size < PAGE_SIZE) {
                    state.isEndReached = true
                }
                _stations.value?.addAll(newStations)
                _stations.notify()
            } catch (e: ShoutcastError) {
                _error.setError(e)
            }
            state.jobMap.remove(tag)
        }
        state.jobMap[tag] = job
    }

    private class SearchState {

        var query: String? = null
        var isEndReached: Boolean = false
        val jobMap: MutableMap<String, Job> = mutableMapOf()

        fun reset(newQuery: String) {
            query = newQuery
            isEndReached = false

            // Cancel all the previous jobs.
            jobMap.forEach { (_, job) -> job.cancel() }
            jobMap.clear()
        }

    }

}

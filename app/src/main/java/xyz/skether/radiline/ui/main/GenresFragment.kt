package xyz.skether.radiline.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_base_main.*
import xyz.skether.radiline.R
import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.service.PlaybackService
import xyz.skether.radiline.ui.base.BaseFragment
import xyz.skether.radiline.ui.base.LayoutId
import xyz.skether.radiline.ui.base.OnLastItemScrollListener
import xyz.skether.radiline.utils.logError
import xyz.skether.radiline.viewmodel.GenresViewModel

@LayoutId(R.layout.fragment_base_main)
class GenresFragment : BaseFragment(), GenresAdapter.Callback {

    private companion object Config {
        const val NUM_LAST_ITEMS = 3
        const val KEY_EXPANDED_GENRES_IDS = "key_expanded_genres_ids"
    }

    private lateinit var genresViewModel: GenresViewModel
    private val adapter = GenresAdapter(this)
    private var currentGenre: Genre? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        genresViewModel = ViewModelProviders.of(this).get(GenresViewModel::class.java)
        if (savedInstanceState != null) {
            val expandedGenresIds = savedInstanceState.getLongArray(KEY_EXPANDED_GENRES_IDS)
            currentGenre = findCurrentGenre(genresViewModel.genres.value, expandedGenresIds!!)
        }

        genresViewModel.genres.observe(this, Observer(::updateData))
        genresViewModel.error.observe(this, Observer(::onError))

        recyclerView.apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addItemDecoration(DividerItemDecoration(context, lm.orientation))
            clearOnScrollListeners()
            addOnScrollListener(OnLastItemScrollListener(NUM_LAST_ITEMS, ::loadMoreStations))
            adapter = this@GenresFragment.adapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLongArray(KEY_EXPANDED_GENRES_IDS, getExpandedGenresIds())
    }

    override fun onGenreSelected(genre: Genre) {
        openGenre(genre)
    }

    override fun onStationSelected(station: Station) {
        context?.apply {
            startService(PlaybackService.playIntent(this, station.id))
        }
    }

    private fun openGenre(genre: Genre) {
        currentGenre = genre
        showItems(genre)
    }

    /**
     * @return true if the genre was closed, false if there is nothing to close.
     */
    fun closeGenre(): Boolean {
        return if (currentGenre != null) {
            currentGenre = currentGenre!!.parent
            showItems(currentGenre)
            true
        } else {
            false
        }
    }

    private fun findCurrentGenre(genres: List<Genre>?, expandedGenresIds: LongArray): Genre? {
        var expandedGenre: Genre? = null
        var subGenres = genres
        for (expandedGenreId in expandedGenresIds) {
            if (subGenres == null) break
            for (genre in subGenres!!) {
                if (expandedGenreId == genre.id) {
                    expandedGenre = genre
                    subGenres = genre.subGenres
                    break
                }
            }
        }
        return expandedGenre
    }

    private fun getExpandedGenresIds(): LongArray {
        val ids = ArrayList<Long>()
        var genre = currentGenre
        while (genre != null) {
            ids.add(0, genre.id)
            genre = genre.parent
        }
        return ids.toLongArray()
    }

    private fun loadMoreStations() {
        if (currentGenre != null) {
            genresViewModel.loadStations(currentGenre!!)
        }
    }

    private fun updateData(rootGenres: List<Genre>) {
        val genres: List<Genre>?
        val stations: List<Station>?
        if (currentGenre == null) {
            genres = rootGenres
            stations = null
        } else {
            genres = currentGenre?.subGenres
            stations = currentGenre?.stations
        }

        val items = mutableListOf<MainItem>()
        genres?.forEach { items.add(GenreMainItem(it)) }
        stations?.forEach { items.add(StationMainItem(it)) }

        adapter.updateData(items)
    }

    private fun onError(error: Throwable?) {
        if (error != null) {
            showSnackbar(R.string.error_loading_data)
            logError(error)
        }
    }

    private fun showItems(genre: Genre?) {
        val genres: List<Genre>?
        var stations: List<Station>? = null
        if (genre == null) {
            genres = genresViewModel.genres.value
            if (genres == null) {
                return
            }
        } else {
            genres = genre.subGenres
            stations = genre.stations
            if (genres == null) {
                genresViewModel.loadSubGenres(genre)
            }
            if (stations == null) {
                genresViewModel.loadStations(genre)
            }
        }

        val items = mutableListOf<MainItem>()
        genres?.forEach { items.add(GenreMainItem(it)) }
        stations?.forEach { items.add(StationMainItem(it)) }

        adapter.setData(items)
    }

}

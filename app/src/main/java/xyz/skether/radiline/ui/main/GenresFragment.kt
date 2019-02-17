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
import xyz.skether.radiline.ui.base.BaseFragment
import xyz.skether.radiline.ui.base.LayoutId
import xyz.skether.radiline.viewmodel.GenresViewModel

@LayoutId(R.layout.fragment_base_main)
class GenresFragment : BaseFragment(), GenresAdapter.Callback {

    private lateinit var genresViewModel: GenresViewModel
    private val adapter = GenresAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        genresViewModel = ViewModelProviders.of(this).get(GenresViewModel::class.java)
        genresViewModel.genres.observe(this, Observer(adapter::updateData))

        recyclerView.apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addItemDecoration(DividerItemDecoration(context, lm.orientation))
            adapter = this@GenresFragment.adapter
        }
    }

    override fun onStationSelected(station: Station) {
        // todo
    }

    override fun onLoadSubGenres(genre: Genre) {
        genresViewModel.loadSubGenres(genre)
    }

    override fun onLoadStations(genre: Genre) {
        genresViewModel.loadStations(genre)
    }

}

package xyz.skether.radiline.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_base_main.*
import xyz.skether.radiline.R
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.service.PlaybackService
import xyz.skether.radiline.ui.base.BaseFragment
import xyz.skether.radiline.ui.base.LayoutId
import xyz.skether.radiline.ui.base.OnLastItemScrollListener
import xyz.skether.radiline.utils.logError
import xyz.skether.radiline.viewmodel.TopStationsViewModel

@LayoutId(R.layout.fragment_base_main)
class TopStationsFragment : BaseFragment(), TopStationsAdapter.Callback {

    private companion object Config {
        const val NUM_LAST_ITEMS = 3
    }

    private lateinit var topStationsViewModel: TopStationsViewModel
    private val adapter = TopStationsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        topStationsViewModel = ViewModelProviders.of(this).get(TopStationsViewModel::class.java)
        topStationsViewModel.stations.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = false
            adapter.updateData(it)
        })
        topStationsViewModel.error.observe(this, Observer(::onError))

        recyclerView.apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addItemDecoration(DividerItemDecoration(context, lm.orientation))
            clearOnScrollListeners()
            addOnScrollListener(OnLastItemScrollListener(NUM_LAST_ITEMS) {
                topStationsViewModel.loadTopStations()
            })
            adapter = this@TopStationsFragment.adapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            topStationsViewModel.loadTopStations(true)
        }
    }

    override fun onStationSelected(station: Station) {
        context?.apply {
            startService(PlaybackService.playIntent(this, station.id))
        }
    }

    private fun onError(error: Throwable?) {
        if (error != null) {
            swipeRefreshLayout.isRefreshing = false
            showSnackbarAllowingSkip(R.string.error_loading_data)
            logError(error)
        }
    }

}

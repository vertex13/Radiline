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
import xyz.skether.radiline.ui.base.BaseFragment
import xyz.skether.radiline.ui.base.LayoutId
import xyz.skether.radiline.viewmodel.TopStationsViewModel

@LayoutId(R.layout.fragment_base_main)
class TopStationsFragment : BaseFragment(), TopStationsAdapter.Callback {

    private lateinit var topStationsViewModel: TopStationsViewModel
    private val adapter = TopStationsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        topStationsViewModel = ViewModelProviders.of(this).get(TopStationsViewModel::class.java)
        topStationsViewModel.stations.observe(this, Observer(adapter::updateData))

        recyclerView.apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addItemDecoration(DividerItemDecoration(context, lm.orientation))
            adapter = this@TopStationsFragment.adapter
        }
    }

    override fun onStationSelected(station: Station) {
        // todo
    }

}

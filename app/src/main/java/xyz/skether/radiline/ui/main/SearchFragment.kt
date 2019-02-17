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
import xyz.skether.radiline.viewmodel.SearchViewModel

@LayoutId(R.layout.fragment_base_main)
class SearchFragment : BaseFragment(), SearchAdapter.Callback {

    private lateinit var searchViewModel: SearchViewModel
    private val adapter = SearchAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        searchViewModel.stations.observe(this, Observer(adapter::updateData))

        recyclerView.apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addItemDecoration(DividerItemDecoration(context, lm.orientation))
            adapter = this@SearchFragment.adapter
        }
    }

    override fun onQueryChanged(query: String) {
        searchViewModel.search(query)
    }

    override fun onStationSelected(station: Station) {
        // todo
    }

}

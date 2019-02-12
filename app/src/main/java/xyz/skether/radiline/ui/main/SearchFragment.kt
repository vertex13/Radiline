package xyz.skether.radiline.ui.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_base_main.*
import xyz.skether.radiline.R
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.BaseFragment
import xyz.skether.radiline.ui.base.LayoutId

@LayoutId(R.layout.fragment_base_main)
class SearchFragment : BaseFragment() {

    private val stations = mockStations()
    private val adapter = SearchAdapter(stations)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@SearchFragment.adapter
        }
    }

    private fun mockStations(): List<Station> {
        val stationList = mutableListOf<Station>()
        for (i in 1..10) {
            stationList.add(Station(i, "Station #$i"))
        }
        return stationList
    }

}

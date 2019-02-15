package xyz.skether.radiline.ui.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_base_main.*
import xyz.skether.radiline.R
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.BaseFragment
import xyz.skether.radiline.ui.base.LayoutId

@LayoutId(R.layout.fragment_base_main)
class TopStationsFragment : BaseFragment() {

    private val stations = mockStations()
    private val adapter = TopStationsAdapter(stations)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addItemDecoration(DividerItemDecoration(context, lm.orientation))
            adapter = this@TopStationsFragment.adapter
        }
    }

    private fun mockStations(): List<Station> {
        val stationList = mutableListOf<Station>()
        for (i in 1..50) {
            stationList.add(Station(i, "Station #$i"))
        }
        return stationList
    }

}

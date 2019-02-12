package xyz.skether.radiline.ui.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_base_main.*
import xyz.skether.radiline.R
import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.Station
import xyz.skether.radiline.ui.base.BaseFragment
import xyz.skether.radiline.ui.base.LayoutId

@LayoutId(R.layout.fragment_base_main)
class GenresFragment : BaseFragment() {

    private val genres = mockGenres()
    private val adapter = GenresAdapter(genres)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@GenresFragment.adapter
        }
    }

    private fun mockGenres(): List<Genre> {
        val genres = mutableListOf<Genre>()
        for (i in 1..10) {
            val genre = Genre(i, "Genre #$i", true)
            genre.subGenres = mutableListOf()
            for (j in 1..3) {
                val subGenre = Genre(i * 1000 + j, "Sub Genre #$j", false, genre)
                subGenre.stations = mutableListOf()
                for (k in 1..5) {
                    subGenre.stations!!.add(Station(i, "Station #$k - Genre $i-$j", subGenre))
                }
                genre.subGenres!!.add(subGenre)
            }
            genre.stations = mutableListOf()
            for (k in 1..2) {
                genre.stations!!.add(Station(i, "Station #$k - Genre $i", genre))
            }
            genres.add(genre)
        }
        return genres
    }

}

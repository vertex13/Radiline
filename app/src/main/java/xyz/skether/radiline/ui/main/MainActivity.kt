package xyz.skether.radiline.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import kotlinx.android.synthetic.main.activity_main.*
import xyz.skether.radiline.R
import xyz.skether.radiline.ui.base.BaseActivity
import xyz.skether.radiline.ui.base.LayoutId
import xyz.skether.radiline.ui.base.showSnackbar

@LayoutId(R.layout.activity_main)
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        playButton.setOnClickListener {
            showSnackbar(rootLayout, "TODO Play and Pause")
        }

        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected)
        navigation.selectedItemId = navigation.selectedItemId
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_previous -> true
        R.id.action_info -> true
        else -> super.onOptionsItemSelected(item)
    }

    private fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.nav_genres -> openFragment(GenresFragment())
        R.id.nav_top -> openFragment(TopStationsFragment())
        R.id.nav_search -> openFragment(SearchFragment())
        else -> false
    }

    private fun openFragment(fragment: Fragment): Boolean {
        supportFragmentManager.transaction {
            replace(R.id.fragmentContainer, fragment)
        }
        return true
    }

}

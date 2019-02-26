package xyz.skether.radiline.ui.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OnLastItemScrollListenerTest {

    // o - is an item in the recycler view
    // [   ] - is a screen
    // [ooo] - is a screen with visible items

    private lateinit var recyclerView: RecyclerView

    @Before
    fun setUp() {
        val lm: LinearLayoutManager = mock {
            on { itemCount } doReturn 10
            //on { findLastVisibleItemPosition() } doReturn 9
        }
        recyclerView = mock {
            on { layoutManager } doReturn lm
        }
    }

    @Test
    fun scrolledToLastItem() {
        // ooooooo[ooo]

        val lm = recyclerView.layoutManager!! as LinearLayoutManager
        whenever(lm.findLastVisibleItemPosition()).thenReturn(9)

        val callback: () -> Unit = mock()
        val scrollListener = OnLastItemScrollListener(callback = callback)
        scrollListener.onScrolled(recyclerView, 0, 0)
        verify(callback).invoke()
    }

    @Test
    fun notScrolledToLastItem() {
        // oooooo[ooo]o

        val lm = recyclerView.layoutManager!! as LinearLayoutManager
        whenever(lm.findLastVisibleItemPosition()).thenReturn(8)

        val callback: () -> Unit = mock()
        val scrollListener = OnLastItemScrollListener(callback = callback)
        scrollListener.onScrolled(recyclerView, 0, 0)
        verify(callback, never()).invoke()
    }

    @Test
    fun scrolledToLast3Items() {
        // ooooo[ooo]oo

        val lm = recyclerView.layoutManager!! as LinearLayoutManager
        whenever(lm.findLastVisibleItemPosition()).thenReturn(7)

        val callback: () -> Unit = mock()
        val scrollListener = OnLastItemScrollListener(3, callback)
        scrollListener.onScrolled(recyclerView, 0, 0)
        verify(callback).invoke()
    }

    @Test
    fun notScrolledToLast3Items() {
        // oooo[ooo]ooo

        val lm = recyclerView.layoutManager!! as LinearLayoutManager
        whenever(lm.findLastVisibleItemPosition()).thenReturn(6)

        val callback: () -> Unit = mock()
        val scrollListener = OnLastItemScrollListener(3, callback)
        scrollListener.onScrolled(recyclerView, 0, 0)
        verify(callback, never()).invoke()
    }

}

package xyz.skether.radiline

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertThat<Int>(2 + 2).isEqualTo(4)
    }

    @Test
    fun contains() {
        assertWithMessage("4 should be in the list.").that<Int>(4).isIn(listOf(1, 3, 4))
    }
}

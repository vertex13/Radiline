package xyz.skether.radiline.data.shoutcast

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PlaylistResponseTest {

    @Test
    fun deserialization() {
        val responseStream = javaClass.getResourceAsStream("/response-playlist.xml")!!
        val result = PlaylistResponse.Deserializer.deserialize(responseStream)
        val sampleTrackList = listOf(
            TrackResponse(
                title = "Dance Wave!",
                location = "http://78.31.65.20:8080/dance.mp3"
            )
        )
        val sampleResponse = PlaylistResponse(sampleTrackList)
        assertThat(result).isEqualTo(sampleResponse)
    }

}

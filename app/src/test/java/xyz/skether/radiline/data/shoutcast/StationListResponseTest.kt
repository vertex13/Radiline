package xyz.skether.radiline.data.shoutcast

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StationListResponseTest {

    @Test
    fun deserialization() {
        val responseStream = javaClass.getResourceAsStream("/response-station-list.xml")!!
        val result = StationListResponse.Deserializer.deserialize(responseStream)
        val sampleTuneIn = mapOf(
            "base" to "/sbin/tunein-station.pls",
            "base-m3u" to "/sbin/tunein-station.m3u",
            "base-xspf" to "/sbin/tunein-station.xspf"
        )
        val sampleStations = listOf(
            StationResponse(
                id = 1826116,
                name = "Hitradio OE3",
                mediaType = "audio/mpeg",
                bitRate = 192,
                genre = "00s",
                currentTrack = "HITRADIO Ö3 - Livestream",
                numberListeners = 33506,
                logo = "https://i3.radionomy.com/radios/200/6/6f2c/6f2ca76b-4d1d-464d-a336-db0913fb5229.jpg"
            ),
            StationResponse(
                id = 1735956,
                name = "COOLfahrenheit 93 - (7)",
                mediaType = "audio/aacp",
                bitRate = 64,
                genre = "Easy Listening",
                currentTrack = "-5B!1H2'L - @+8@42'2!@+2",
                numberListeners = 11815,
                logo = "http://i.radionomy.com/document/radios/a/a265/a2654f5f-669e-4b9e-ac13-962971886ad2.jpg"
            ),
            StationResponse(
                id = 1794490,
                name = "COOLfahrenheit 93 -- (6)",
                mediaType = "audio/aacp",
                bitRate = 64,
                genre = "Easy Listening",
                currentTrack = "The TOYS - STARS",
                numberListeners = 9430,
                logo = "http://i.radionomy.com/document/radios/7/7c48/7c48c133-a1ee-42e6-97fa-6420420e8cad.jpg"
            )
        )
        val sampleResponse = StationListResponse(sampleTuneIn, sampleStations)
        assertThat(result).isEqualTo(sampleResponse)
    }

}

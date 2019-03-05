package xyz.skether.radiline.data.shoutcast

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StationListResponseTest {

    @Test
    fun legacyDeserialization() {
        val responseStream = javaClass.getResourceAsStream("/response-station-list-legacy.xml")!!
        val xmlResponse = StationListResponse.LegacyDeserializer.deserialize(responseStream)
        responseStream.close()
        val sampleTuneInList = listOf(
            TuneInResponse("base", "/sbin/tunein-station.pls"),
            TuneInResponse("base-m3u", "/sbin/tunein-station.m3u"),
            TuneInResponse("base-xspf", "/sbin/tunein-station.xspf")
        )
        val sampleStations = listOf(
            StationResponse(
                id = 1826116,
                name = "Hitradio OE3",
                mediaType = "audio/mpeg",
                bitRate = 192,
                genreName = "00s",
                currentTrack = "HITRADIO Ö3 - Livestream",
                numberListeners = 33506,
                logo = "https://i3.radionomy.com/radios/200/6/6f2c/6f2ca76b-4d1d-464d-a336-db0913fb5229.jpg"
            ),
            StationResponse(
                id = 1735956,
                name = "COOLfahrenheit 93 - (7)",
                mediaType = "audio/aacp",
                bitRate = 64,
                genreName = "Easy Listening",
                currentTrack = "-5B!1H2'L - @+8@42'2!@+2",
                numberListeners = 11815,
                logo = "http://i.radionomy.com/document/radios/a/a265/a2654f5f-669e-4b9e-ac13-962971886ad2.jpg"
            ),
            StationResponse(
                id = 1794490,
                name = "COOLfahrenheit 93 -- (6)",
                mediaType = "audio/aacp",
                bitRate = 64,
                genreName = "Easy Listening",
                currentTrack = "The TOYS - STARS",
                numberListeners = 9430,
                logo = "http://i.radionomy.com/document/radios/7/7c48/7c48c133-a1ee-42e6-97fa-6420420e8cad.jpg"
            )
        )
        val sampleResponse = StationListResponse(sampleTuneInList, sampleStations)
        assertThat(xmlResponse).isEqualTo(sampleResponse)
    }

    @Test
    fun deserialization() {
        val responseStream = javaClass.getResourceAsStream("/response-station-list.xml")!!
        val xmlResponse = StationListResponse.Deserializer.deserialize(responseStream)
        responseStream.close()
        val sampleTuneInList = listOf(
            TuneInResponse("base", "/sbin/tunein-station.pls"),
            TuneInResponse("base-m3u", "/sbin/tunein-station.m3u"),
            TuneInResponse("base-xspf", "/sbin/tunein-station.xspf")
        )
        val sampleStations = listOf(
            StationResponse(
                id = 1439332,
                name = "AlienWare",
                mediaType = "audio/mpeg",
                bitRate = 128,
                genreName = "Misc",
                currentTrack = "Los Terricolas - Lloraras",
                numberListeners = 878,
                logo = "http://i.radionomy.com/document/radios/a/a159/a159432b-ec58-4ac7-aaa4-4cb28f2d2d92.gif"
            ),
            StationResponse(
                id = 1650132,
                name = "1.FM - Blues (www.1.fm)",
                mediaType = "audio/mpeg",
                bitRate = 64,
                genreName = "Blues",
                currentTrack = null,
                numberListeners = 667,
                logo = "http://i.radionomy.com/document/radios/c/c271/c2710241-fc18-4baf-b3d8-bd41e2cea649.jpg"
            ),
            StationResponse(
                id = 1796807,
                name = "YoungArts Tecnologia",
                mediaType = "audio/aacp",
                bitRate = 64,
                genreName = "Acoustic Blues",
                currentTrack = null,
                numberListeners = 352,
                logo = "http://i.radionomy.com/document/radios/f/f8d3/f8d30ef6-cbcc-41b9-8bd5-ecd21b24f254.png"
            )
        )
        val sampleResponse = StationListResponse(sampleTuneInList, sampleStations)
        assertThat(xmlResponse).isEqualTo(sampleResponse)
    }

}

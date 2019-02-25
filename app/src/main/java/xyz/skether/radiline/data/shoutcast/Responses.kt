package xyz.skether.radiline.data.shoutcast

import com.github.kittinunf.fuel.core.ResponseDeserializable
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

data class StationListResponse(
    val tuneIn: Map<String, String>,
    val stations: List<StationResponse>
) {

    object Deserializer : ResponseDeserializable<StationListResponse> {
        override fun deserialize(inputStream: InputStream): StationListResponse {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream)
            val root = doc.firstChild as Element

            val tuneIn = mutableMapOf<String, String>()
            val tuneInAttrs = root.getElementsByTagName("tunein").item(0).attributes
            for (i in 0 until tuneInAttrs.length) {
                val node = tuneInAttrs.item(i)
                tuneIn[node.nodeName] = node.nodeValue
            }

            val stations = mutableListOf<StationResponse>()
            val stationNodes = root.getElementsByTagName("station")
            for (i in 0 until stationNodes.length) {
                stations.add(stationFromNode(stationNodes.item(i)))
            }

            return StationListResponse(tuneIn, stations)
        }
    }

}

data class StationResponse(
    val id: Int,
    val name: String,
    val mediaType: String,
    val bitRate: Int,
    val genre: String,
    val currentTrack: String?,
    val numberListeners: Int,
    val logo: String?
)

private fun stationFromNode(node: Node): StationResponse {
    val attrs = node.attributes
    return StationResponse(
        id = attrs.nodeValue("id").toInt(),
        name = attrs.nodeValue("name"),
        mediaType = attrs.nodeValue("mt"),
        bitRate = attrs.nodeValue("br").toInt(),
        genre = attrs.nodeValue("genre"),
        currentTrack = attrs.nodeValueOpt("ct"),
        numberListeners = attrs.nodeValue("lc").toInt(),
        logo = attrs.nodeValueOpt("logo")
    )
}

data class GenreListResponse(
    val genres: List<GenreResponse>
) {

    object Deserializer : ResponseDeserializable<GenreListResponse> {
        override fun deserialize(inputStream: InputStream): GenreListResponse {
            val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream)
            val root = doc.firstChild as Element
            val dataElement = root.getElementsByTagName("data").item(0) as Element
            val genreListElement = dataElement.getElementsByTagName("genrelist").item(0) as Element

            val genres = mutableListOf<GenreResponse>()
            val genreNodes = genreListElement.getElementsByTagName("genre")
            for (i in 0 until genreNodes.length) {
                genres.add(genreFromNode(genreNodes.item(i)))
            }

            return GenreListResponse(genres)
        }
    }

}

data class GenreResponse(
    val id: Int,
    val name: String,
    val hasChildren: Boolean
)

private fun genreFromNode(node: Node): GenreResponse {
    val attrs = node.attributes
    return GenreResponse(
        id = attrs.nodeValue("id").toInt(),
        name = attrs.nodeValue("name"),
        hasChildren = attrs.nodeValue("haschildren").toBoolean()
    )
}

private fun NamedNodeMap.nodeValue(name: String): String = getNamedItem(name).nodeValue

private fun NamedNodeMap.nodeValueOpt(name: String): String? = getNamedItem(name)?.nodeValue
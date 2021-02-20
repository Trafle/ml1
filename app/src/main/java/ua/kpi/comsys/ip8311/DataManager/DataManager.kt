package ua.kpi.comsys.ip8311

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.beust.klaxon.Klaxon
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class DataManager {

    companion object {

        fun fetchBooksFromWeb(searchText: String): WebBooksObj? {
            // Check if the string is at least 3 chars
            if(searchText.length < 3 || containsSpecChars(searchText)) return null

            val url = "https://api.itbook.store/1.0/search/" + searchText
            val response = createURLConnection(url) ?: return null
            if (response[10] != '0') return null // Check if there is such a book. The 10th char must be the amount of errors in the response
            return Klaxon().parse<WebBooksObj>(response) ?: error("invalid book JSON")
        }

        fun fetchBookInfoFromWeb(isbn13: String): BookInfo? {

            // Check if the isbn13 is valid
            val serialPattern = "[0-9]{13}".toRegex()
            if (!serialPattern.matches(isbn13))  return null

            val url = "https://api.itbook.store/1.0/books/" + isbn13
            val response = createURLConnection(url) ?: return null
            if (response[10] != '0') return null // Check if there is such a book. The 10th char must be the amount of errors in the response
            return Klaxon().parse<BookInfo>(response) ?: error("invalid isbn13 bookInfo JSON")
        }

        fun fetchImageInfoFromCacheOrWeb(REQUEST: String, COUNT: Int, context: Context): MutableList<WebImageInfo>? {
            val API_KEY = "19193969-87191e5db266905fe8936d565"
            val url = "https://pixabay.com/api/?key=$API_KEY&q=$REQUEST&image_type=photo&per_page=$COUNT"

            // Check db
            var webImageInfoList: MutableList<WebImageInfo>? = mutableListOf<WebImageInfo>()
            val dbHandler = MyDBHandler(context, null, null, 1)
            webImageInfoList = dbHandler.findCollageImages(url)
            if (webImageInfoList?.size != null && webImageInfoList?.size != 0) return webImageInfoList

            // If there is internet connection - fetch data
            val response: String
            val imageResponse: WebImageResponse

            response = createURLConnection(url) ?: return null
            imageResponse = Klaxon().parse<WebImageResponse>(response) ?: return null


            // Add images to db
            for (imageInfo in imageResponse.hits) dbHandler.addImageOfCollage(imageInfo, url)

            val ims = dbHandler.findCollageImages(url)

            return imageResponse.hits
        }

        fun getBitmapFromURL(src: String): Bitmap? {
            // Try to retrieve it from cache
            var bitmap = Cacher.getBitmap(src)
            if(bitmap != null) return bitmap

            // If none in cache - fetch and save
            val input: InputStream
            try {
                val connection = URL(src).openConnection()
                connection.doInput = true
                input = connection.getInputStream()
            } catch (e: Exception) {
                return null
            }

            bitmap = BitmapFactory.decodeStream(input)
            Cacher.saveBitmap(src, bitmap)
            return bitmap
        }

        private fun createURLConnection(url: String): String? {
            return try {
                val openConnection: URLConnection = URL(url).openConnection()
                openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36") // Set the agent
                val response = openConnection.getInputStream().bufferedReader().use { it.readText() } // Read the response as a string
                response
            } catch (e: Exception) {
                null
            }
        }

        fun containsSpecChars(string: String): Boolean {
            // If there is something except latin letters and spaces return true
            val pattern = Regex("""[^a-zA-Z .,]""")
            return pattern.containsMatchIn(string)
        }
    }
}

//fun main () {
//    val books = mutableListOf<String>("s", "k")
//    books.forEach {
//        print(it)
//    }
//}


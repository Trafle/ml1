package ua.kpi.comsys.ip8311

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.beust.klaxon.Klaxon
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class WebImageResponse(val total: Int, val totalHits: Int, val hits: MutableList<WebImageInfo>) {}

data class WebImageInfo( val id: Int = 0, val pageURL: String = "", val type: String = "", val tags: String = "", val previewURL: String = "",
                         val previewWidth: Int = 0, val previewHeight: Int = 0, val webformatURL: String = "", val webformatWidth: Int = 0,
                         val webformatHeight: Int = 0, val largeImageURL: String = "", val imageWidth: Int = 0, val imageHeight: Int = 0,
                         val imageSize: Int = 0, val views: Int = 0, val downloads: Int = 0, val favorites: Int = 0, val likes: Int = 0,
                         val comments: Int = 0, val user_id: Int = 0, val user: String = "", val userImageURL: String = "") {}

class JSONBooksObj(val books: MutableList<Book>) {}

data class Book(val title: String = "", val subtitle: String = "", val isbn13: String = "",
                val price: String = "", val image: String = "", var bookInfo: BookInfo? = BookInfo()) {
    fun validate(book: Book): Book {

        // Declare Book Properties
        var title = book.title
        var subtitle = book.subtitle
        var isbn13 = book.isbn13
        var price = book.price
        var image: String = book.image

        // Set Length Limits For Text Views
        val titleLength = 50
        val subtitleLength = 75

        if (book.title.length > titleLength) {
            title = book.title.substring(0, titleLength)
            title += "..."
        }

        if (book.subtitle.length > subtitleLength) {
            subtitle = book.subtitle.substring(0, subtitleLength)
            subtitle += "..."
        }

        if (book.subtitle == "") subtitle = "No description"

        val isbn13Pattern = "[0-9]{13}".toRegex()
        if (!isbn13Pattern.matches(book.isbn13)) {
            isbn13 = "NO ISBN13"
        }

        val pricePattern = "[a-zA-Z]*".toRegex()
        if (pricePattern.matches(book.price)) {
            price = "PRICELESS"
        } else {
            price = book.price
        }

        return Book(title, subtitle, isbn13, price, image)
    }
}

class BookInfo(var title: String = "", var subtitle: String = "", var isbn13: String = "",
               var price: String = "", var image: String = "", var authors: String = "",
               var publisher: String = "", var pages: String = "", var year: String = "",
               var rating: String = "", var desc: String = "", var error: String = "0", var url: String = "", var isbn10: String = "", var language: String = "") {}

class WebBooksObj(val books: MutableList<Book> = mutableListOf<Book>(), val error: String = "", val page: String = "", val total: String = "")



class DataReader {

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

        fun fetchImageInfoFromWeb(REQUEST: String, COUNT: Int): WebImageResponse? {
            val API_KEY = "19193969-87191e5db266905fe8936d565"
            val url = "https://pixabay.com/api/?key=$API_KEY&q=$REQUEST&image_type=photo&per_page=$COUNT"
            val response = createURLConnection(url) ?: return null
            val imageResponse = Klaxon().parse<WebImageResponse>(response) ?: error("invalid WebImageResponse JSON")
            return imageResponse
        }

        fun getBitmapFromURL(src: String): Bitmap? {
            val connection = URL(src).openConnection()
            connection.doInput = true
            val input: InputStream = connection.getInputStream()
            return BitmapFactory.decodeStream(input)
        }

        private fun createURLConnection(url: String): String? {
            val openConnection: URLConnection = URL(url).openConnection()
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36") // Set the agent
            val response = openConnection.getInputStream().bufferedReader().use { it.readText() } // Read the response as a string
            return response
        }

        fun containsSpecChars(string: String): Boolean {
            // If there is something except latin letters and spaces return true
            val pattern = Regex("""[^a-zA-Z .,]""")
            return pattern.containsMatchIn(string)
        }
    }
}

//fun main () {
//    print("-= ${DataReader.containsSpecChars("-=")}\n")
//    print("o.s-d ${DataReader.containsSpecChars("o.s-d")}\n")
//    print("\\sdf ${DataReader.containsSpecChars("\\sdf")}\n")
//    print("sad asd ${DataReader.containsSpecChars("sad as, .d")}\n")
//}


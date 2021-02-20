package ua.kpi.comsys.ip8311

class WebImageResponse(val total: Int, val totalHits: Int, val hits: MutableList<WebImageInfo>) {}

data class WebImageInfo(
    var id: Int = 0, var pageURL: String = "", var type: String = "", var tags: String = "", var previewURL: String = "",
    var previewWidth: Int = 0, var previewHeight: Int = 0, var webformatURL: String = "", var webformatWidth: Int = 0,
    var webformatHeight: Int = 0, var largeImageURL: String = "", var imageWidth: Int = 0, var imageHeight: Int = 0,
    var imageSize: Int = 0, var views: Int = 0, var downloads: Int = 0, var favorites: Int = 0, var likes: Int = 0,
    var comments: Int = 0, var user_id: Int = 0, var user: String = "", var userImageURL: String = "") {}

class JSONBooksObj(val books: MutableList<Book>) {}

data class Book(var title: String = "", var subtitle: String = "", var isbn13: String = "",
                var price: String = "", var image: String = "", var bookInfo: BookInfo? = BookInfo()) {
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

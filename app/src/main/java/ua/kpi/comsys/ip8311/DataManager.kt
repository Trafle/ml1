package ua.kpi.comsys.ip8311

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.beust.klaxon.Klaxon
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

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

class MyDBHandler(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) :
        SQLiteOpenHelper(context, DATABASE_NAME,
                factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "labBase.db"
        private val DATABASE_VERSION = 1

        // Table books
        val TABLE_BOOKS = "books"

        val COLUMN_BOOKS_ID = "_id"
        val COLUMN_BOOKS_TITLE = "title"
        val COLUMN_BOOKS_SUBTITLE = "subtitle"
        val COLUMN_BOOKS_ISBN13 = "isbn13"
        val COLUMN_BOOKS_PRICE = "price"
        val COLUMN_BOOKS_IMAGE = "image"

        // Table collage
        val TABLE_COLLAGE = "collage"

        val COLUMN_COLLAGE_REQUEST = "url_request"
        val COLUMN_COLLAGE_ID = "id"
        val COLUMN_COLLAGE_DBID = "_id"
        val COLUMN_COLLAGE_PAGE_URL = "pageURL"
        val COLUMN_COLLAGE_TYPE = "type"
        val COLUMN_COLLAGE_TAGS = "tags"
        val COLUMN_COLLAGE_PREVIEW_URL = "previewURL"
        val COLUMN_COLLAGE_PREVIEW_WIDTH = "previewWidth"
        val COLUMN_COLLAGE_PREVIEW_HEIGHT = "previewHeight"
        val COLUMN_COLLAGE_WEBFORMAT_URL = "webformatURL"
        val COLUMN_COLLAGE_WEBFORMAT_WIDTH = "webformatWidth"
        val COLUMN_COLLAGE_WEBFORMAT_HEIGHT = "webformatHeight"
        val COLUMN_COLLAGE_LARGE_IMAGE_URL = "largeImageURL"
        val COLUMN_COLLAGE_IMAGE_WIDTH = "imageWidth"
        val COLUMN_COLLAGE_IMAGE_HEIGHT = "imageHeight"
        val COLUMN_COLLAGE_IMAGE_SIZE = "imageSize"
        val COLUMN_COLLAGE_VIEWS = "views"
        val COLUMN_COLLAGE_DOWNLOADS = "downloads"
        val COLUMN_COLLAGE_FAVORITES = "favorites"
        val COLUMN_COLLAGE_LIKES = "likes"
        val COLUMN_COLLAGE_COMMENTS = "comments"
        val COLUMN_COLLAGE_USER_ID = "user_id"
        val COLUMN_COLLAGE_USER = "user"
        val COLUMN_COLLAGE_USER_IMAGE_URL = "userImageURL"
    }

    fun addImageOfCollage(webImageInfo: WebImageInfo, url: String): Boolean {
        val values = ContentValues()
        values.put(COLUMN_COLLAGE_REQUEST, url)
        values.put(COLUMN_COLLAGE_ID, webImageInfo.id)
        values.put(COLUMN_COLLAGE_PAGE_URL, webImageInfo.pageURL)
        values.put(COLUMN_COLLAGE_TYPE, webImageInfo.type)
        values.put(COLUMN_COLLAGE_TAGS, webImageInfo.tags)
        values.put(COLUMN_COLLAGE_PREVIEW_URL, webImageInfo.previewURL)
        values.put(COLUMN_COLLAGE_PREVIEW_WIDTH, webImageInfo.previewWidth)
        values.put(COLUMN_COLLAGE_PREVIEW_HEIGHT, webImageInfo.previewHeight)
        values.put(COLUMN_COLLAGE_WEBFORMAT_URL, webImageInfo.webformatURL)
        values.put(COLUMN_COLLAGE_WEBFORMAT_WIDTH, webImageInfo.webformatWidth)
        values.put(COLUMN_COLLAGE_WEBFORMAT_HEIGHT, webImageInfo.webformatHeight)
        values.put(COLUMN_COLLAGE_LARGE_IMAGE_URL, webImageInfo.largeImageURL)
        values.put(COLUMN_COLLAGE_IMAGE_WIDTH, webImageInfo.imageWidth)
        values.put(COLUMN_COLLAGE_IMAGE_HEIGHT, webImageInfo.imageHeight)
        values.put(COLUMN_COLLAGE_IMAGE_SIZE, webImageInfo.imageSize)
        values.put(COLUMN_COLLAGE_VIEWS, webImageInfo.views)
        values.put(COLUMN_COLLAGE_DOWNLOADS, webImageInfo.downloads)
        values.put(COLUMN_COLLAGE_FAVORITES, webImageInfo.favorites)
        values.put(COLUMN_COLLAGE_LIKES, webImageInfo.likes)
        values.put(COLUMN_COLLAGE_COMMENTS, webImageInfo.comments)
        values.put(COLUMN_COLLAGE_USER_ID, webImageInfo.user_id)
        values.put(COLUMN_COLLAGE_USER, webImageInfo.user)
        values.put(COLUMN_COLLAGE_USER_IMAGE_URL, webImageInfo.userImageURL)

        val db = this.writableDatabase

        try {
            db.insert(TABLE_COLLAGE, null, values)
        } catch (e: Exception) {
            print(e)
        }

        db.close()
        return true
    }

    fun addBook(book: Book): Boolean {
        val values = ContentValues()
        values.put(COLUMN_BOOKS_TITLE, book.title)
        values.put(COLUMN_BOOKS_SUBTITLE, book.subtitle)
        values.put(COLUMN_BOOKS_ISBN13, book.isbn13)
        values.put(COLUMN_BOOKS_PRICE, book.price)
        values.put(COLUMN_BOOKS_IMAGE, book.image)

        val db = this.writableDatabase
        db.insert(TABLE_BOOKS, null, values)
        db.close()
        return true
    }

    fun findCollageImages(url: String): MutableList<WebImageInfo>? {
        val query = "SELECT * FROM $TABLE_COLLAGE WHERE $COLUMN_COLLAGE_REQUEST = \"$url\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        val webImageInfoList: MutableList<WebImageInfo> = mutableListOf<WebImageInfo>()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val webImageinfo = WebImageInfo()

                webImageinfo.id = Integer.parseInt(cursor.getString(2))
                webImageinfo.pageURL = cursor.getString(3)
                webImageinfo.type = cursor.getString(4)
                webImageinfo.tags = cursor.getString(5)
                webImageinfo.previewURL = cursor.getString(6)
                webImageinfo.previewWidth = Integer.parseInt(cursor.getString(7))
                webImageinfo.previewHeight = Integer.parseInt(cursor.getString(8))
                webImageinfo.webformatURL = cursor.getString(9)
                webImageinfo.webformatWidth = Integer.parseInt(cursor.getString(10))
                webImageinfo.webformatHeight = Integer.parseInt(cursor.getString(11))
                webImageinfo.largeImageURL = cursor.getString(12)
                webImageinfo.imageWidth = Integer.parseInt(cursor.getString(13))
                webImageinfo.imageHeight = Integer.parseInt(cursor.getString(14))
                webImageinfo.imageSize = Integer.parseInt(cursor.getString(15))
                webImageinfo.views = Integer.parseInt(cursor.getString(16))
                webImageinfo.downloads = Integer.parseInt(cursor.getString(17))
                webImageinfo.favorites = Integer.parseInt(cursor.getString(18))
                webImageinfo.likes = Integer.parseInt(cursor.getString(19))
                webImageinfo.comments = Integer.parseInt(cursor.getString(20))
                webImageinfo.user_id = Integer.parseInt(cursor.getString(21))
                webImageinfo.user = cursor.getString(22)
                webImageinfo.userImageURL = cursor.getString(23)

                webImageInfoList.add(webImageinfo)
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return if (webImageInfoList.size == 0) null else webImageInfoList
    }

    fun findBooks(titlePiece: String): MutableList<Book>? {
        val query = "SELECT * FROM $TABLE_BOOKS WHERE $COLUMN_BOOKS_TITLE LIKE '%$titlePiece%'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        val books: MutableList<Book> = mutableListOf<Book>()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val book = Book()

                book.title = cursor.getString(1)
                book.subtitle = cursor.getString(2)
                book.isbn13 = cursor.getString(3)
                book.price = cursor.getString(4)
                book.image = cursor.getString(5)

                books.add(book)
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return if (books.size == 0) null else books
    }

    fun findBook(bookTitle: String): Book? {
        val query = "SELECT * FROM $TABLE_BOOKS WHERE $COLUMN_BOOKS_TITLE = \"$bookTitle\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var book: Book? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            // columnindex == 1 to skip the id field
            val title = cursor.getString(1)
            val subtitle = cursor.getString(2)
            val isbn13 = cursor.getString(3)
            val price = cursor.getString(4)
            val image = cursor.getString(5)

            book = Book(title, subtitle, isbn13, price, image)
            cursor.close()
        }

        db.close()
        return book
    }

    fun deleteBook(bookTitle: String): Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_BOOKS WHERE $COLUMN_BOOKS_TITLE = \"$bookTitle\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_BOOKS, COLUMN_BOOKS_ID + " = $id", null)
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    fun dropTablesIfExist() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COLLAGE")
        onCreate(db)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_BOOKS_TABLE = "CREATE TABLE $TABLE_BOOKS (" +
                "$COLUMN_BOOKS_ID INTEGER PRIMARY KEY," +
                "$COLUMN_BOOKS_TITLE TEXT," +
                "$COLUMN_BOOKS_SUBTITLE TEXT," +
                "$COLUMN_BOOKS_ISBN13 TEXT," +
                "$COLUMN_BOOKS_PRICE TEXT," +
                "$COLUMN_BOOKS_IMAGE TEXT)"

        val CREATE_COLLAGE_TABLE = "CREATE TABLE $TABLE_COLLAGE (" +
                "$COLUMN_COLLAGE_DBID INTEGER PRIMARY KEY," +
                "$COLUMN_COLLAGE_REQUEST TEXT," +
                "$COLUMN_COLLAGE_ID INTEGER," +
                "$COLUMN_COLLAGE_PAGE_URL TEXT," +
                "$COLUMN_COLLAGE_TYPE TEXT," +
                "$COLUMN_COLLAGE_TAGS TEXT," +
                "$COLUMN_COLLAGE_PREVIEW_URL TEXT," +
                "$COLUMN_COLLAGE_PREVIEW_WIDTH INTEGER," +
                "$COLUMN_COLLAGE_PREVIEW_HEIGHT INTEGER," +
                "$COLUMN_COLLAGE_WEBFORMAT_URL TEXT," +
                "$COLUMN_COLLAGE_WEBFORMAT_WIDTH INTEGER," +
                "$COLUMN_COLLAGE_WEBFORMAT_HEIGHT INTEGER," +
                "$COLUMN_COLLAGE_LARGE_IMAGE_URL TEXT," +
                "$COLUMN_COLLAGE_IMAGE_WIDTH INTEGER," +
                "$COLUMN_COLLAGE_IMAGE_HEIGHT INTEGER," +
                "$COLUMN_COLLAGE_IMAGE_SIZE INTEGER," +
                "$COLUMN_COLLAGE_VIEWS INTEGER," +
                "$COLUMN_COLLAGE_DOWNLOADS INTEGER," +
                "$COLUMN_COLLAGE_FAVORITES INTEGER," +
                "$COLUMN_COLLAGE_LIKES INTEGER," +
                "$COLUMN_COLLAGE_COMMENTS INTEGER," +
                "$COLUMN_COLLAGE_USER_ID INTEGER," +
                "$COLUMN_COLLAGE_USER TEXT," +
                "$COLUMN_COLLAGE_USER_IMAGE_URL TEXT)"

        db.execSQL(CREATE_BOOKS_TABLE)
        db.execSQL(CREATE_COLLAGE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COLLAGE")
        onCreate(db)
    }
}

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
            var bitmap = cache?.get(src)
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
            BitmapCache.instance.saveBitmapToCache(src, bitmap)
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


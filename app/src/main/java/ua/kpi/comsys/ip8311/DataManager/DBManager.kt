package ua.kpi.comsys.ip8311

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
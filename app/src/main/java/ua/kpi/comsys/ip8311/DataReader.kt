package ua.kpi.comsys.ip8311

import android.content.Context
import android.graphics.drawable.Drawable
import com.beust.klaxon.Klaxon
import java.io.InputStream

class JSONBooksObj(val books: MutableList<Book>)

class DataReader {
    companion object {
        fun parseJson(file: String, context: Context): MutableList<Book> {

            // Read The Books
            val jsonBooks = context.assets.open(file).bufferedReader().use { it.readText() }
            val JSONbooks = Klaxon().parse<JSONBooksObj>(jsonBooks) ?: error("invalid book JSON")

            // ISBN13 Validation Pattern
            val serialPattern = "[0-9]{13}".toRegex()

            // Load information from according isbn13 files
            for (book in JSONbooks.books) {

                // If There Is No According File
                if(!serialPattern.matches(book.isbn13)) continue

                // Read The Book Info And Assign It To The Current Book
                val bookJSONInfo = context.assets.open("bookInfo/" + book.isbn13 + ".txt")
                    .bufferedReader().use { it.readText() }
                val info = Klaxon().parse<BookInfo>(bookJSONInfo) ?: error("invalid book info JSON")
                book.bookInfo = info
            }

            return JSONbooks.books
        }

        fun readImage(name: String, ctx: Context): Drawable {
            val ims: InputStream = ctx.assets.open(name)
            return Drawable.createFromStream(ims, null)
        }
    }
}
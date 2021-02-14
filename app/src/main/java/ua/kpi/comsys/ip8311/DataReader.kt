package ua.kpi.comsys.ip8311

import android.content.Context
import android.graphics.drawable.Drawable
import com.beust.klaxon.Klaxon
import java.io.File
import java.io.InputStream


class JSONBooksObj(val books: MutableList<Book>)

class DataReader {
    companion object {
        fun parseJson(file: String, context: Context): MutableList<Book> {

            // Read The Books
            val jsonBooks = context.assets.open(file).bufferedReader().use { it.readText() }
            val JSONbooks = Klaxon().parse<JSONBooksObj>(jsonBooks) ?: error("invalid book JSON")

            // Load information from according isbn13 files
            for (book in JSONbooks.books) {
                val info: BookInfo? = readISBN13(book.isbn13, context)
                if(info == null) continue
                else book.bookInfo = info
            }
            return JSONbooks.books
        }

        fun readImage(name: String, ctx: Context): Drawable {
            var fileName: String = name
            if (fileName == "") fileName = "noimage.png"
            val ims: InputStream = ctx.assets.open(fileName)
            return Drawable.createFromStream(ims, null)
        }

        fun readISBN13(number: String, context: Context): BookInfo? {
            val serialPattern = "[0-9]{13}".toRegex()
            if (!serialPattern.matches(number)) return null
            val filePath = "bookInfo/" + number + ".txt"

//            val bookJSONInfo: String = File(filePath).inputStream().readBytes().toString(Charsets.UTF_8)
            val bookJSONInfo = context.assets.open(filePath)
                .bufferedReader().use { it.readText() }
            return Klaxon().parse<BookInfo>(bookJSONInfo)
        }
    }
}
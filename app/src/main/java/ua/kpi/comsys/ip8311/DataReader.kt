package ua.kpi.comsys.ip8311

import android.content.Context
import com.beust.klaxon.Klaxon
import java.io.InputStream

class JSONBooksObj(val books: MutableList<Book>)

class DataReader {
    companion object {
        fun parseJson(file: String, context: Context): MutableList<Book> {
            val inputStream: InputStream = context.assets.open(file)
            val jsonBooks = inputStream.bufferedReader().use { it.readText() }
            val books = Klaxon().parse<JSONBooksObj>(jsonBooks)
            if (books != null) {
                return books.books
            } else {
                return mutableListOf(Book("", "", "", "", ""))
            }
        }
    }
}
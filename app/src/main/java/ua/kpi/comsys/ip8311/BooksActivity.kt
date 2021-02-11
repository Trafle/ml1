package ua.kpi.comsys.ip8311

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import androidx.fragment.app.Fragment

class BooksActivity : Fragment () {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.books_layout, container, false)
        context?.let { addTableRows(view, it) }
        return view
    }
}

fun addTableRows(view: View, ctx: Context): Unit {
    val books = DataReader.parseJson("jsonBooks.txt", ctx)
    val bookAdapter = BookAdapter(ctx, books)
    println(books)
    var tableLayout: TableLayout = view.findViewById(R.id.tableLayout)
    for (i in books.indices) {
        val row = TableRow(ctx)
        row.visibility = View.VISIBLE
        val bookElement = bookAdapter.getView(i, null, row)
        tableLayout.addView(bookElement)
    }
}
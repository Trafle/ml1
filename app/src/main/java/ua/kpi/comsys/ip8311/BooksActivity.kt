package ua.kpi.comsys.ip8311

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

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
    var recycler: RecyclerView = view.findViewById(R.id.booksRecylerView)
    recycler.adapter = BookAdapter(ctx, books)
    recycler.setHasFixedSize(true)
}
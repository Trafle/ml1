package ua.kpi.comsys.ip8311

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class BooksActivity : Fragment () {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.books_layout, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the page in parallel independent of the main thread
        CoroutineScope(IO).launch {

            // Read The File In Coroutine
            if (context == null) error("context == null")
            val bookSet = DataReader.parseJson("jsonBooks.txt", requireContext())
            addTableRows(view, requireContext(), bookSet)

        }
    }
}

//fun BooksActivity.filterItems(query: String?, recycler: RecyclerView) {
//
//    // Creating Regexp Pattern
//    val decapQuery = query?.toLowerCase()?.removeSurrounding(" ", " ")
//    val searchPattern = (".*$decapQuery.*").toRegex()
//
//    // Hide all unwanted elements
//    var i: Int = 0
//    var itemView: View? = recycler.layoutManager?.findViewByPosition(i)
//    while(itemView != null) {
//
//        itemView = recycler.layoutManager?.findViewByPosition(i)
//
//        val bookTitle = itemView.getView
//        i++
//    }
//
//    recycler.adapter.getItemCount().forEachIndexed { i, book ->
//
//        if (bookTitle.matches(searchPattern)) {
//            recycler.layoutManager?.viewfindViewByPosition(i)?.visibility = View.VISIBLE
//        } else {
//            recycler.layoutManager?.findViewByPosition(i)?.visibility = View.GONE
//        }
//    }
//}

fun BooksActivity.initSearchBarEvents(searchBar: SearchView, adapter: BookAdapter) {

    searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
//            filterItems(query, recycler)
            return query != null
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            adapter.filter.filter(newText)
//            filterItems(newText, recycler)
            return newText != null
        }

    })
}

suspend fun BooksActivity.addTableRows(view: View, ctx: Context, bookSet: MutableList<Book>): Unit {
    // Update UI in Main Thread
    withContext(Main) {

        // Remove the "loading" icon when loaded up
        val loadingProp: TextView = view.findViewById(R.id.loadingProp)
        (loadingProp.getParent() as ViewManager).removeView(loadingProp)

        // Bind the Adapter and Layout manager to recycler
        val recycler: RecyclerView = view.findViewById(R.id.booksRecylerView)
        val adapter =  BookAdapter(ctx, bookSet)
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)
        val loManager = LinearLayoutManager(ctx)
        recycler.layoutManager  = loManager

        // Initialize the search capability
        val searchBar = view.findViewById<SearchView>(R.id.searchBar)
        initSearchBarEvents(searchBar, adapter)
    }
}

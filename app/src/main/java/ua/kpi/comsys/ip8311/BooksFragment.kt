package ua.kpi.comsys.ip8311

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import com.tsuryo.swipeablerv.SwipeableRecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class BooksFragment : Fragment () {

    var adapter: BookAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.books_layout, container, false)

        // Bind the Adapter and Layout manager to recycler
        val recycler: SwipeableRecyclerView = view.findViewById(R.id.booksRecylerView)
//        recycler.setNestedScrollingEnabled(false)
        val loManager = LinearLayoutManager(context)
        var dataset = mutableListOf<Book>()
        adapter = context?.let { BookAdapter(it, dataset) }
        recycler.adapter = adapter
        recycler.layoutManager  = loManager
        recycler.setHasFixedSize(true)

        // Set Swipe To Delete Listener
        recycler.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedLeft(position: Int) {
                adapter?.dataset?.removeAt(position)
                adapter?.notifyItemRemoved(position)
            }

            override fun onSwipedRight(position: Int) {
                TODO("No functionality required")
            }
        })

        // Load the page data in parallel independent of the main thread
        CoroutineScope(IO).launch {
            // Read The File In Coroutine
            if (context == null) error("context == null")
            val bookSet = DataReader.parseJson("jsonBooks.txt", requireContext())
            adapter?.let { addRecyclerRows(view, it, bookSet) }
        }

        // Set Button Onclick Listener
        val addButton: ImageButton = view.findViewById(R.id.addBookButton)
        addButton.setOnClickListener {
            val i = Intent(context, AddBookActivity::class.java)
            startActivityForResult(i, 1)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Get the extras
        val title = data?.getStringExtra("title")
        val subtitle = data?.getStringExtra("subtitle")
        var price = data?.getStringExtra("price")

        if(title == null || subtitle == null || price == null) {return}

        // create a book object
        price = "$$price"
        val book = Book(title, subtitle, "", price, "", null)

        adapter?.dataset?.add(book)
        adapter?.dataset?.let { adapter?.notifyItemInserted(it.size) }
    }
}

fun BooksFragment.initSearchBarEvents(searchBar: SearchView) {

    searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean {
            return query != null
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            adapter?.filter?.filter(newText)
            return newText != null
        }

    })
}

suspend fun BooksFragment.addRecyclerRows(view: View, adapter: BookAdapter, bookSet: MutableList<Book>): Unit {
    // Update UI in Main Thread
    withContext(Main) {

        // Remove the "loading" icon when loaded up
        val loadingProp: TextView = view.findViewById(R.id.loadingProp)
        (loadingProp.getParent() as ViewManager).removeView(loadingProp)

        // Initialize the search capability
        val searchBar = view.findViewById<SearchView>(R.id.searchBar)
//        searchBar.background = null
        initSearchBarEvents(searchBar)

        // Add fetched data to the adapter
        adapter.dataset = bookSet
        adapter.datasetFiltered = bookSet
        adapter.notifyDataSetChanged()
    }
}

package ua.kpi.comsys.ip8311

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
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
        val loManager = LinearLayoutManager(context)
        val dataset = mutableListOf<Book>()
        adapter = context?.let { BookAdapter(it, dataset) }
        recycler.adapter = adapter
        recycler.layoutManager = loManager
        recycler.setHasFixedSize(true)

        // Set Swipe To Delete Listener
        recycler.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedLeft(position: Int) {
                adapter?.dataset?.removeAt(position)
                adapter?.dataset?.get(position)?.let { removeBook(it.title) }
                adapter?.notifyItemRemoved(position)
            }

            override fun onSwipedRight(position: Int) {
                TODO("No functionality required")
            }
        })

        // Initialize the search capability
        val searchBar = view.findViewById<SearchView>(R.id.searchBar)
        initSearchBarEvents(searchBar)

        // Set Button Onclick Listener
        val addButton: ImageButton = view.findViewById(R.id.addBookButton)
        addButton.setOnClickListener {
            val i = Intent(context, AddBookActivity::class.java)
            startActivityForResult(i, 1)
        }

//        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) }
//        dbHandler?.dropTablesIfExist()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Get the extras
        val title = data?.getStringExtra("title")
        val subtitle = data?.getStringExtra("subtitle")
        var price = data?.getStringExtra("price")

        if (title == null || subtitle == null || price == null) {
            return
        }

        // create a book object
        price = "$$price"
        val book = Book(title, subtitle, "", price, "", null)

        adapter?.dataset?.add(book)
        adapter?.dataset?.let { adapter?.notifyItemInserted(it.size) }
    }

    fun addBookToDB(book: Book): Boolean {
        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) } ?: return false
        return dbHandler.addBook(book)
    }

    fun findBooksInDB(titlePiece: String): MutableList<Book>? {
        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) } ?: return null
        return dbHandler.findBooks(titlePiece)
    }

    fun findBookInDB(title: String): Book? {
        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) }
        val book = dbHandler?.findBook(title) ?: return null
        return book
    }

    fun removeBook(title: String) {
        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) }
        dbHandler?.deleteBook(title)
    }

    fun BooksFragment.initSearchBarEvents(searchBar: SearchView) {

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return query != null
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "") {
                    adapter?.dataset?.clear()
                    adapter?.datasetFiltered?.clear()
                    adapter?.notifyDataSetChanged()
                }
                if (newText == null || newText.length < 3) return false

                // Launch search in parallel
                val loadingProp = view?.findViewById<View>(R.id.loadingProp)
                loadingProp?.visibility = View.VISIBLE
                CoroutineScope(IO).launch {changeRecyclerRows(loadingProp, newText)}
                return true
            }

        })
    }

    suspend fun BooksFragment.changeRecyclerRows(loadingProp: View?, newText: String): Unit {

        // First check if there are such books in caches and if not - fetch from the web
        var bookSet = findBooksInDB(newText)
        if (bookSet == null) {
            bookSet = DataManager.fetchBooksFromWeb(newText)?.books
            // Add the newly fetched books to the cache
            if (bookSet != null) {
                for(book in bookSet) {
                    addBookToDB(book)
                }
            }
        }

        // Update the adapter
        if (bookSet != null) {
            adapter?.dataset = bookSet
            adapter?.datasetFiltered = bookSet
        }

        // Update UI in the Main Thread
        withContext(Main) {
            loadingProp?.visibility = View.GONE
            adapter?.notifyDataSetChanged()
            if (bookSet?.size == 0 || bookSet == null) Toast.makeText(context,"No Books Found", Toast.LENGTH_SHORT).show()
        }
    }
}

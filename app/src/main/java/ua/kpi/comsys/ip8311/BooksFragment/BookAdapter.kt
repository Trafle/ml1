package ua.kpi.comsys.ip8311

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class BookAdapter(
        private val context: Context,
        var dataset: MutableList<Book>
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>(), Filterable {

    var datasetFiltered = mutableListOf<Book>()

    init {
        datasetFiltered = dataset
    }

    class BookViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val relativeLayoutView = view.findViewById(R.id.relLay) as View
        val titleTextView = view.findViewById(R.id.title) as TextView
        val subtitleTextView = view.findViewById(R.id.subtitle) as TextView
        val priceTextView = view.findViewById(R.id.price) as TextView
        val bookIconImageView = view.findViewById(R.id.image) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val bookItemLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.book_item, parent, false)
        return BookViewHolder(bookItemLayout)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

        // Beautify the book
        var book = Book()
        book = book.validate(datasetFiltered[position])

        // Set a click event
        holder.relativeLayoutView.setOnClickListener {
            val intent = Intent(context, BookInfoActivity::class.java)
            intent.putExtra("isbn13", book.isbn13)
            context.startActivity(intent)
        }

        // Set Properties
        holder.titleTextView.text = (book.title)
        holder.subtitleTextView.text = book.subtitle
        holder.priceTextView.text = (book.price)

        if (book.image == "") holder.bookIconImageView.setImageBitmap(BitmapFactory.decodeStream(context.assets.open("no_picture.png")))
        else {
            CoroutineScope(IO).launch {
                val bitmap = DataManager.getBitmapFromURL(book.image)
                withContext(Dispatchers.Main) {
                    holder.bookIconImageView.setPadding(-50)
                    holder.bookIconImageView.setImageBitmap(bitmap)
                }
            }
        }
        holder.priceTextView.setTextColor(context.resources.getColor(R.color.red, context.resources.newTheme()))
    }

    override fun getFilter(): Filter {

        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                if (charSearch.isEmpty()) {
                    datasetFiltered = dataset
                } else {
                    val resultList = mutableListOf<Book>()
                    for (book in dataset) {
                        if (book.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(book)
                        }
                    }
                    datasetFiltered = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = datasetFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                datasetFiltered = results?.values as MutableList<Book>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = datasetFiltered.size
}
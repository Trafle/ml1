package ua.kpi.comsys.ip8311

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class BookInfo (var title: String = "", var subtitle: String = "", var isbn13: String = "",
                var price: String = "", var image: String = "", var authors: String = "",
                var publisher: String = "", var pages: String = "", var year: String = "",
                var rating: String = "", var desc: String = "") {}

data class Book(val title: String = "", val subtitle: String = "", val isbn13: String = "",
                    val price: String = "", val image: String = "", var bookInfo: BookInfo? = BookInfo()) {

    fun validate(book: Book): Book {

        // Declare Book Properties
        var title = book.title
        var subtitle = book.subtitle
        var isbn13 = book.isbn13
        var price = book.price
        var image: String = book.image

        // Set Length Limits For Text Views
        val titleLength = 50
        val subtitleLength = 75

        if (book.title.length > titleLength) {
            title = book.title.substring(0, titleLength)
            title += "..."
        }

        if (book.subtitle.length > subtitleLength) {
            subtitle = book.subtitle.substring(0, subtitleLength)
            subtitle += "..."
        }

        if (book.subtitle == "") subtitle = "No description"

        val isbn13Pattern = "[0-9]{13}".toRegex()
        if (!isbn13Pattern.matches(book.isbn13)) {
            isbn13 = "NO ISBN13"
        }

        val pricePattern = "[a-zA-Z]*".toRegex()
        if (pricePattern.matches(book.price)) {
            price = "PRICELESS"
        } else {
            price = book.price
        }
        //        price = book.price.capitalize()

        val imagePattern = "Image_[0-9]{2}.png".toRegex()
        if (!imagePattern.matches(book.image)) {
            image = "noimage.png"
        }
        image =  image.decapitalize()


        return Book(title, subtitle, isbn13, price, image)
    }
}

class BookAdapter(
        private val context: Context,
        var dataset: MutableList<Book>
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>(), Filterable {

//    override fun notifyItemInserted


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
        holder.bookIconImageView.setImageDrawable(DataReader.readImage(book.image, context))

        // Set Price Colors If Supported By Android Version
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            when(holder.priceTextView.text){
                "PRICELESS" -> holder.priceTextView.setTextColor(context.resources.getColor(R.color.yellow, context.resources.newTheme()))
                else -> holder.priceTextView.setTextColor(context.resources.getColor(R.color.red, context.resources.newTheme()))
            }
        }
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
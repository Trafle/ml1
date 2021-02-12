package ua.kpi.comsys.ip8311

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStream

data class Book(val title: String = "", val subtitle: String = "", val isbn13: String = "",
                    val price: String = "", val image: String = "") {

    companion object {
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

    inner class BookInfo (val authors: String = "", val publisher: String = "", val pages: String = "",
                    val year: String = "", val rating: String = "", val desc: String = "") {}
}

class BookAdapter(
        private val context: Context,
        private val dataset: List<Book>
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView = view.findViewById(R.id.title) as TextView
        val subtitleTextView = view.findViewById(R.id.subtitle) as TextView
        val priceTextView = view.findViewById(R.id.price) as TextView
        val bookIconImageView = view.findViewById(R.id.image) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.book_item, parent, false)

        return BookViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = Book.validate(dataset[position])
        // Set Properties
        holder.titleTextView.text = (book.title)
        holder.subtitleTextView.text = book.subtitle
        holder.priceTextView.text = (book.price)
        holder.bookIconImageView.setImageDrawable(readImage(book.image, context))

        // Set Price Colors If Supported By Android Version
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            when(holder.priceTextView.text){
                "PRICELESS" -> holder.priceTextView.setTextColor(context.resources.getColor(R.color.yellow, context.resources.newTheme()))
                else -> holder.priceTextView.setTextColor(context.resources.getColor(R.color.red, context.resources.newTheme()))
            }
        }
    }

    override fun getItemCount(): Int = dataset.size

    fun readImage(name: String, ctx: Context = context): Drawable {
        val ims: InputStream = context.assets.open(name)
        return Drawable.createFromStream(ims, null)
    }
}
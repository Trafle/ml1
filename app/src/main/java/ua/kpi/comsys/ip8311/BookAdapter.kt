package ua.kpi.comsys.ip8311

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import java.io.InputStream

data class Book(val title: String = "", val subtitle: String = "", val isbn13: String = "", val price: String = "", val image: String = "") {}

class BookAdapter (val context: Context, private val dataSource: MutableList<Book>) : BaseAdapter () {
    init {
        for(i in dataSource.indices) {
            dataSource[i] = beautify(dataSource[i])
        }
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.book_item, parent, false)

        val titleTextView = view.findViewById(R.id.title) as TextView
        val subtitleTextView = view.findViewById(R.id.subtitle) as TextView
        val priceTextView = view.findViewById(R.id.price) as TextView
        val bookIconImageView = view.findViewById(R.id.image) as ImageView

        val titleTypeFace = ResourcesCompat.getFont(context, R.font.robotocondensed_light)
        titleTextView.typeface = titleTypeFace
        subtitleTextView.typeface = titleTypeFace
        priceTextView.typeface = titleTypeFace

        val book = getItem(position) as Book
        titleTextView.text = book.title
        subtitleTextView.text = book.subtitle
        priceTextView.text = book.price

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            when(priceTextView.text){
                "PRICELESS" -> priceTextView.setTextColor(context.resources.getColor(R.color.yellow, context.resources.newTheme()))
                else -> priceTextView.setTextColor(context.resources.getColor(R.color.red, context.resources.newTheme()))
            }
        }

        titleTextView.setTextSize(17F)
        subtitleTextView.setTextSize(14F)
        priceTextView.setTextSize(14F)

        val ims: InputStream = context.assets.open(book.image)
        val d = Drawable.createFromStream(ims, null)
        bookIconImageView.setImageDrawable(d)

        var ps = -35 // Padding Size
        bookIconImageView.setPadding(ps,ps,ps,ps)
        return view
    }

    fun beautify(book: Book): Book {
        var title = book.title
        var subtitle = book.subtitle
        var isbn13 = book.isbn13
        var price = book.price
        var image: String = book.image

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

        val isbn13Pattern = "[0-9]{13}".toRegex()
        if(!isbn13Pattern.matches(book.isbn13)) {
            isbn13 = "NO ISBN13"
        }

        val pricePattern = "[a-zA-Z]*".toRegex()
        if(pricePattern.matches(book.price)) {
            price = "PRICELESS"
        } else {
            price = book.price
        }
//        price = book.price.capitalize()

        val imagePattern = "Image_[0-9]{2}.png".toRegex()
        if(!imagePattern.matches(book.image)) {
            image = "ioimage.png"
        }

        image = "i" + image.substring(1, image.length)

        return Book(title, subtitle, isbn13, price, image)
    }

}

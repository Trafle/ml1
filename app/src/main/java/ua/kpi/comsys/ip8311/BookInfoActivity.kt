package ua.kpi.comsys.ip8311

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class BookInfoActivity (): AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_info_layout)

        val isbn13: String = intent.getStringExtra("isbn13").toString()

        // Read the file (may make it read in parallel later)
        val bookInfo: BookInfo? = DataReader.readISBN13(isbn13, this)

        if (bookInfo != null) {
            findViewById<ImageView>(R.id.image).setImageDrawable(DataReader.readImage(bookInfo.image.toLowerCase(), this))
            findViewById<TextView>(R.id.title).text = stylizeSpan("Title", bookInfo.title)
            findViewById<TextView>(R.id.subtitle).text = stylizeSpan("Subtitle", bookInfo.subtitle)
            findViewById<TextView>(R.id.desc).text = stylizeSpan("Description", bookInfo.desc)
            findViewById<TextView>(R.id.authors).text = stylizeSpan("Authors", bookInfo.authors)
            findViewById<TextView>(R.id.publisher).text = stylizeSpan("Publisher", bookInfo.publisher)
            findViewById<TextView>(R.id.pages).text = stylizeSpan("Pages", bookInfo.pages)
            findViewById<TextView>(R.id.year).text = stylizeSpan("Year", bookInfo.year)
            findViewById<TextView>(R.id.rating).text = stylizeSpan("Rating", bookInfo.rating)
        }

        // Filling up elements of the layout
    }
}

fun stylizeSpan(title: String, text: String?): Spannable {
    val mBSpannableString: Spannable = SpannableString(title + ": " + text)
    mBSpannableString.setSpan(ForegroundColorSpan(Color.GRAY),0, title.length + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    return mBSpannableString
}
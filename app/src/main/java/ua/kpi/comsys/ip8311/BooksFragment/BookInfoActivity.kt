package ua.kpi.comsys.ip8311

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class BookInfoActivity (): AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_info_layout)

        val isbn13 = intent.getStringExtra("isbn13").toString()

        val mainLayout = findViewById<View>(R.id.mainLayout)
        val loadingProp = findViewById<View>(R.id.loadingProp2)
        loadingProp.visibility = View.VISIBLE

//      Filling up elements of the layout asyncly
        CoroutineScope(Dispatchers.IO).launch {fillInfo(mainLayout, isbn13, loadingProp)}
    }
}

fun stylizeSpan(title: String, text: String?): Spannable {
    val mBSpannableString: Spannable = SpannableString(title + ": " + text)
    mBSpannableString.setSpan(ForegroundColorSpan(Color.GRAY),0, title.length + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    return mBSpannableString
}

suspend fun fillInfo(mainLayout: View, isbn13: String, loadingProp: View): Boolean {
    val bookInfo = DataManager.fetchBookInfoFromWeb(isbn13)
    val bitmapImage = bookInfo?.let { DataManager.getBitmapFromURL(it.image) }

    withContext(Dispatchers.Main) {
        mainLayout.visibility = View.VISIBLE
        loadingProp.visibility = View.GONE
        if (bookInfo != null) {
            mainLayout.findViewById<ImageView>(R.id.image).setImageBitmap(bitmapImage)
            mainLayout.findViewById<TextView>(R.id.title).text = stylizeSpan("Title", bookInfo.title)
            mainLayout.findViewById<TextView>(R.id.subtitle).text = stylizeSpan("Subtitle", bookInfo.subtitle)
            mainLayout.findViewById<TextView>(R.id.desc).text = stylizeSpan("Description", bookInfo.desc)
            mainLayout.findViewById<TextView>(R.id.authors).text = stylizeSpan("Authors", bookInfo.authors)
            mainLayout.findViewById<TextView>(R.id.publisher).text = stylizeSpan("Publisher", bookInfo.publisher)
            mainLayout.findViewById<TextView>(R.id.pages).text = stylizeSpan("Pages", bookInfo.pages)
            mainLayout.findViewById<TextView>(R.id.year).text = stylizeSpan("Year", bookInfo.year)
            mainLayout.findViewById<TextView>(R.id.rating).text = stylizeSpan("Rating", bookInfo.rating)
        }
    }
    return true
}


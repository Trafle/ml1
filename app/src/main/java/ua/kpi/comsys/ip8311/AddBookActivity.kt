package ua.kpi.comsys.ip8311

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText


class AddBookActivity (): AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addbook)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val title: String = findViewById<AppCompatEditText>(R.id.titleEdit).text.toString()
            val subtitle: String = findViewById<AppCompatEditText>(R.id.subtitleEdit).text.toString()
            val price: String = findViewById<AppCompatEditText>(R.id.priceEdit).text.toString()

            val returnIntent = Intent()
            returnIntent.putExtra("title", title)
            returnIntent.putExtra("subtitle", subtitle)
            returnIntent.putExtra("price", price)
            setResult(Activity.RESULT_OK, returnIntent);
            finish()
        }
    }
}
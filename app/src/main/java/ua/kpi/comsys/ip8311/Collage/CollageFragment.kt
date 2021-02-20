package ua.kpi.comsys.ip8311

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import android.provider.MediaStore
import android.widget.Toast


class CollageFragment : Fragment() {

    private var adapter: CollageAdapter? = null
    private val REQUEST_IMAGE_CODE = 1

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.collage_fragment, container, false)


        val pic = resources.getDrawable(R.drawable.add_button, context?.theme)
        val recyclerView = view.findViewById<RecyclerView>(R.id.collageRecyclerView)

        // Assign adapter and layout manager
        val loManager = SpannedGridLayoutManager(orientation = SpannedGridLayoutManager.Orientation.VERTICAL, spans = 4)

        loManager.spanSizeLookup = SpannedGridLayoutManager.SpanSizeLookup { position ->
            when (position % 8){
                1 -> SpanSize(3, 3)
                else ->
                    SpanSize(1, 1)
            }
        }

        val imageStack = mutableListOf<Bitmap>()
        loManager.itemOrderIsStable = true
        recyclerView.layoutManager = loManager
        adapter = context?.let { CollageAdapter(it, imageStack) }
        recyclerView.adapter = adapter

        // Init button events
        val button: ImageButton = view.findViewById(R.id.addPic)
        button.setOnClickListener {
            val photoPickerIntent: Intent = Intent(Intent.ACTION_PICK);
            photoPickerIntent.type = "image/*";
            startActivityForResult(photoPickerIntent, REQUEST_IMAGE_CODE);
        }

        // Async read images from the cache or web
        val ctx = context
        CoroutineScope(Dispatchers.IO).launch {
            // First check if there are images in the cache

            // Fetch The images In Coroutine
            if (context == null) error("context == null")
            adapter?.let {
                if (ctx != null) {
                    fillImages(it, view, ctx)
                }
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
            adapter!!.imageStack.add(bitmap)
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {}
}

// Fills the recycler view with images asynchronously
suspend fun fillImages(adapter: CollageAdapter, view: View, ctx: Context): Boolean {
    val imageInfo = DataManager.fetchImageInfoFromCacheOrWeb("hot+summer", 24, ctx)

    // Add the retrieved images to the view
    if (imageInfo != null) {
        for((index, image) in imageInfo.withIndex()) {
            // If no bitmap could be fetched, use "no_picture.png" from assets
            val bitmap = imageInfo[index].previewURL.let { DataManager.getBitmapFromURL(it) } ?:
            BitmapFactory.decodeStream(ctx.assets.open("no_picture.png"))
            adapter.imageStack.add(bitmap)
        }
    }

    withContext(Dispatchers.Main) {
        // Hiding the loading prop
        val loadingProp: View = view.findViewById(R.id.loadingProp)
        loadingProp.visibility = View.GONE
        adapter.notifyDataSetChanged()

        // Notify if no images were retrieved
        if (imageInfo == null) Toast.makeText(ctx,"No Collage Images Found", Toast.LENGTH_SHORT).show()
    }
    return true
}

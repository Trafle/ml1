package ua.kpi.comsys.ip8311

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


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
                1 -> SpanSize(3,3)
                else ->
                    SpanSize(1, 1)
            }
        }

        val imageStack = mutableListOf<Uri>()
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

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            val imageView = view?.findViewById(R.id.imageView) as ImageView?
//            imageView?.setImageURI(data.data)
            adapter!!.imageStack.add(data.data)
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {}
}
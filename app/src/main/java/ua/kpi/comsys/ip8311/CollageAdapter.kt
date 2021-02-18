package ua.kpi.comsys.ip8311

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CollageAdapter(
        private val context: Context,
        var imageStack: MutableList<Bitmap>
) : RecyclerView.Adapter<CollageAdapter.CollageViewHolder>() {

    class CollageViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val imageView = view.findViewById(R.id.imageView) as ImageView
        var rowNumber: Int = 0
        var colNumber: Int = 0
        var rowWeight: Int = 1
        var colWeight: Int = 1
        var rowSpan: Int = 1
        var colSpan: Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollageAdapter.CollageViewHolder {
        val collageSetLayout = LayoutInflater.from(parent.context).inflate(R.layout.collage_item_layout, parent, false)
        return CollageAdapter.CollageViewHolder(collageSetLayout)
    }

    override fun onBindViewHolder(holder: CollageAdapter.CollageViewHolder, position: Int) {
//        itemsCount++

        // init variables
        val relativePosition = position % 8
        var rowNumber: Int = 0
        var colNumber: Int = 0
        var rowWeight: Int = 1
        var colWeight: Int = 1
        var rowSpan: Int = 1
        var colSpan: Int = 1

        // Calculate the elemet's properties based on position
        when (relativePosition) {
            0 -> {rowNumber = 0; colNumber = 0}
            1 -> {rowNumber = 1; colNumber = 0}
            2 -> {rowNumber = 2; colNumber = 0}
            3 -> {rowNumber = 0; colNumber = 1; rowWeight = 3; colWeight = 3; rowSpan = 3; colSpan = 3}
            4 -> {rowNumber = 3; colNumber = 0}
            5 -> {rowNumber = 3; colNumber = 1}
            6 -> {rowNumber = 3; colNumber = 2}
            7 -> {rowNumber = 3; colNumber = 3}
            else -> error("out of range")
        }
//
//        // Change the layout parameters
        val params: GridLayout.LayoutParams = GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 2, null, rowWeight.toFloat()); // in the 0th row with a span of 2
        params.columnSpec = GridLayout.spec(0, 2, null, colWeight.toFloat());
////        params.
//
        holder.imageView.layoutParams = params
        holder.imageView.setImageBitmap(imageStack[position])

        // Assign the properties
//        holder.rowNumber = rowNumber
//        holder.colNumber = colNumber
//        holder.rowWeight = rowWeight
//        holder.colWeight = colWeight
//        holder.rowSpan = rowSpan
//        holder.colSpan = colSpan
    }

    override fun getItemCount(): Int = imageStack.size
}

//class CustomCollageLayout() : GridLayout
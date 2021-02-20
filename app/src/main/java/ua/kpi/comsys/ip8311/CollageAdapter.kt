package ua.kpi.comsys.ip8311

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CollageAdapter(private val context: Context, var imageStack: MutableList<Bitmap>)
    :RecyclerView.Adapter<CollageAdapter.CollageViewHolder>() {

    class CollageViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val imageView = view.findViewById(R.id.imageView) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollageViewHolder {
        val collageSetLayout = LayoutInflater.from(parent.context).inflate(R.layout.collage_item_layout, parent, false)
        return CollageViewHolder(collageSetLayout)
    }

    override fun onBindViewHolder(holder: CollageViewHolder, position: Int) {
        holder.imageView.setImageBitmap(imageStack[position])
    }

    override fun getItemCount(): Int = imageStack.size
}
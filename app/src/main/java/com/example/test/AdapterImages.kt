package com.example.test

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class AdapterImages: RecyclerView.Adapter<ImageViewHolder>() {

    var selectedImagePath = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = selectedImagePath[position]
        holder.image.setImageBitmap(BitmapFactory.decodeFile(imagePath))

    }

    override fun getItemCount(): Int {
        return selectedImagePath.size
    }

    fun addSelectedImages(images: List<String>) {
        this.selectedImagePath = images
        notifyDataSetChanged()
    }
}

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val image: ImageView = view.findViewById(R.id.images)

}
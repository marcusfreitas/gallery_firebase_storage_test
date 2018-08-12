package com.example.marcusfreitas.gallerytestapp.main.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.marcusfreitas.gallerytestapp.R
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import kotlinx.android.synthetic.main.gallery_list_item.view.*

class GalleryAdapter(private var uploadImageList: List<UploadedImage>,
                     private val glide: RequestManager) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return uploadImageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(uploadImageList[position], glide)
    }

    fun setDataSource(data: List<UploadedImage>) {
        uploadImageList = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(uploadedImage: UploadedImage, glide: RequestManager) {
            glide.load(uploadedImage.url).into(itemView.thumbnail)
        }
    }

}
package com.example.examplebotomnav.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.examplebotomnav.databinding.LayBeritaBinding

class AdapterMain(
    private val listNews: ArrayList<News>) : RecyclerView.Adapter<AdapterMain.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = LayBeritaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, photo) = listNews[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.binding.imgNews)
        holder.binding.tvwTitle.text = name
        holder.binding.tvwDesc.text = description
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listNews[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listNews.size

    class ListViewHolder(val binding: LayBeritaBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: News)
    }
}
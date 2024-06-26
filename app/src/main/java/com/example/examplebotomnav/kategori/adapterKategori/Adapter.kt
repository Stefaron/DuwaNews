package com.example.examplebotomnav.kategori.adapterKategori

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.examplebotomnav.DetailNews
import com.example.examplebotomnav.databinding.LayBeritaBinding
import com.example.examplebotomnav.kategori.responseKategori.ResultsItem

class Adapter(
    private val listNews: ArrayList<ResultsItem>,
    private val context: Context
) : RecyclerView.Adapter<Adapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = LayBeritaBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val news = listNews[position]
        Glide.with(holder.itemView.context)
            .load(news.imageUrl)
            .into(holder.imgNews)
        holder.titleNews.text = news.title
        holder.deskNews.text = news.pubDate
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailNews::class.java).apply {
                this.putExtra(DetailNews.EXTRA_ARTICLE_ID, news.articleId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listNews.size

    class ListViewHolder(val binding: LayBeritaBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgNews: ImageView = binding.imgNews
        val titleNews: TextView = binding.tvwTitle
        val deskNews: TextView = binding.tvwDesc
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ResultsItem)
    }

    fun setData(data: ArrayList<ResultsItem?>?) {
        listNews.clear()
        listNews.addAll(data?.filterNotNull() ?: emptyList())
        notifyDataSetChanged()
    }
}

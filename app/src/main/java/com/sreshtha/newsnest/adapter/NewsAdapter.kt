package com.sreshtha.newsnest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sreshtha.newsnest.databinding.ItemArticlePreviewBinding
import com.sreshtha.newsnest.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.BreakingNewsViewHolder>() {

    inner class BreakingNewsViewHolder(val binding:ItemArticlePreviewBinding):RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object:DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakingNewsViewHolder {
        return BreakingNewsViewHolder(
            ItemArticlePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BreakingNewsViewHolder, position: Int) {
        holder.binding.apply {
            tvDate.text = differ.currentList[position].publishedAt
            tvTitle.text = differ.currentList[position].title
            tvDescription.text = differ.currentList[position].description
            val imageUrl = differ.currentList[position].urlToImage
            Glide.with(holder.itemView)
                .load(imageUrl)
                .into(ivImage)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
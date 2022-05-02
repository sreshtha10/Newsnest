package com.sreshtha.newsnest.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.sreshtha.newsnest.databinding.ItemArticlePreviewBinding
import com.sreshtha.newsnest.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.BreakingNewsViewHolder>() {

    companion object {
        const val STRING_PREF_NAME = "USER_SETTINGS"
        const val STRING_IS_LANG_ENG = "IS_LANG_ENG"
    }


    inner class BreakingNewsViewHolder(val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakingNewsViewHolder {

        val holder = ItemArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BreakingNewsViewHolder(holder)
    }

    override fun onBindViewHolder(holder: BreakingNewsViewHolder, position: Int) {
        holder.binding.apply {
            tvDate.text = differ.currentList[position].publishedAt
            val sharedPreferences = root.context.getSharedPreferences(
                STRING_PREF_NAME, Context.MODE_PRIVATE
            )
            if (!sharedPreferences.getBoolean(STRING_IS_LANG_ENG, false)) {
                translateString(holder, position, differ.currentList[position].title)
            } else {
                tvTitle.text = differ.currentList[position].title
            }

            val imageUrl = differ.currentList[position].urlToImage
            Glide.with(holder.itemView)
                .load(imageUrl)
                .into(ivImage)
        }


        holder.binding.root.setOnClickListener {
            try {
                onItemClickListener?.let { it(differ.currentList[position]) }
            } catch (e: Exception) {
                Log.d("TAG", "Article cannot be opened")
                //show snackbar
            }

        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }


    private fun translateString(holder: BreakingNewsViewHolder, position: Int, string: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.HINDI)
            .build()
        val englishHindiTranslator = Translation.getClient(options)

        try {
            englishHindiTranslator.translate(string)
                .addOnSuccessListener {
                    holder.binding.apply {
                        tvTitle.text = it.toString()
                    }
                }
                .addOnFailureListener {
                    holder.binding.apply {
                        tvTitle.text = string
                    }
                }
        } catch (e: Exception) {
            holder.binding.tvTitle.text = string
            Log.d("TRANSLATION_ERROR", e.toString())
            //todo toast
        }
    }


}
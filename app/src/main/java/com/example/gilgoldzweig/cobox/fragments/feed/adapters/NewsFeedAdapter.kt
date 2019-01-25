package com.example.gilgoldzweig.cobox.fragments.feed.adapters

import android.content.Context
import android.support.annotation.IdRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gilgoldzweig.cobox.R
import com.example.gilgoldzweig.cobox.fragments.feed.adapters.diffutil.NewsDiffCallback
import com.example.gilgoldzweig.cobox.models.news.NewsHolder
import com.example.gilgoldzweig.cobox.ui.GlideApp
import me.toptas.rssconverter.RssItem

class NewsFeedAdapter(context: Context,
                      var news: NewsHolder = NewsHolder(),
                      var onItemClicked: (RssItem) -> Unit) :
        RecyclerView.Adapter<NewsFeedAdapter.NewsFeedItemViewHolder>() {

    private val glide = GlideApp.with(context)
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedAdapter.NewsFeedItemViewHolder =
            NewsFeedItemViewHolder(inflater.inflate(R.layout.item_news_feed, parent, false))

    override fun onBindViewHolder(holder: NewsFeedAdapter.NewsFeedItemViewHolder, position: Int) {

        val rssItem = news[position]

        with(holder) {
            if (rssItem.image != null) {
                glide.load(rssItem.image)
                        .centerCrop()
                        .into(newsFeedItemImage)
            }


            newsFeedItemTitle.text = rssItem.title ?: "No title"
            newsFeedItemDescription.text = rssItem.description

            itemView.setOnClickListener {
                onItemClicked(rssItem)
            }
        }
    }

    override fun onViewRecycled(holder: NewsFeedAdapter.NewsFeedItemViewHolder) {
        with(holder) {
            glide.clear(newsFeedItemImage)
            newsFeedItemTitle.text = null
            newsFeedItemDescription.text = null
        }
    }

    override fun getItemCount(): Int = news.size

//    fun updateNews(newsHolder: NewsHolder, checkDiff: Boolean = true) {
//        synchronized(this.news) {
//            this.news = newsHolder
//            if (checkDiff) {
//                val diffResult = DiffUtil.calculateDiff(NewsDiffCallback(news, newsHolder))
//                diffResult.dispatchUpdatesTo(this)
//            } else {
//                notifyDataSetChanged()
//            }
//        }
//    }

    fun updateNews(newsHolder: NewsHolder, diffResult: DiffUtil.DiffResult? = null) {
        this.news = newsHolder
        if (diffResult == null) {
            notifyDataSetChanged()
        } else {
            diffResult.dispatchUpdatesTo(this)
        }
    }

    class NewsFeedItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val newsFeedItemImage: AppCompatImageView = findView(R.id.news_feed_item_image)

        val newsFeedItemTitle: AppCompatTextView = findView(R.id.news_feed_item_title)

        val newsFeedItemDescription: AppCompatTextView = findView(R.id.news_feed_item_description)


        private fun <T : View> findView(@IdRes id: Int): T = itemView.findViewById(id)
    }
}
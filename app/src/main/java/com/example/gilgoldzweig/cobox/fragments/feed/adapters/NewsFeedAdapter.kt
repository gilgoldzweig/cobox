package com.example.gilgoldzweig.cobox.fragments.feed.adapters

import android.content.Context
import android.support.annotation.IdRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gilgoldzweig.cobox.R
import com.example.gilgoldzweig.cobox.models.news.NewsHolder
import me.toptas.rssconverter.RssItem

class NewsFeedAdapter(context: Context,
                      var news: NewsHolder = NewsHolder(),
                      var onItemClicked: (RssItem) -> Unit) :
        RecyclerView.Adapter<NewsFeedAdapter.NewsFeedItemViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedAdapter.NewsFeedItemViewHolder =
            NewsFeedItemViewHolder(inflater.inflate(R.layout.item_news_feed, parent, false))

    override fun onBindViewHolder(holder: NewsFeedAdapter.NewsFeedItemViewHolder, position: Int) {

        val rssItem = news[position]

        with(holder) {

            newsFeedItemTitle.text = rssItem.title ?: "No title"
            newsFeedItemDescription.text = rssItem.description

            itemView.setOnClickListener {
                onItemClicked(rssItem)
            }
        }
    }

    override fun onViewRecycled(holder: NewsFeedAdapter.NewsFeedItemViewHolder) {
        with(holder) {
            newsFeedItemTitle.text = null
            newsFeedItemDescription.text = null
        }
    }

    override fun getItemCount(): Int = news.size

    fun updateNews(newsHolder: NewsHolder, diffResult: DiffUtil.DiffResult? = null) {
        this.news = newsHolder
        if (diffResult == null) {
            notifyDataSetChanged()
        } else {
            diffResult.dispatchUpdatesTo(this)
        }
    }

    class NewsFeedItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val newsFeedItemTitle: AppCompatTextView = findView(R.id.news_feed_item_title)

        val newsFeedItemDescription: AppCompatTextView = findView(R.id.news_feed_item_description)


        private fun <T : View> findView(@IdRes id: Int): T = itemView.findViewById(id)
    }
}
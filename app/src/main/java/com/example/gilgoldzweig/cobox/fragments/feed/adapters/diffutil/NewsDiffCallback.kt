package com.example.gilgoldzweig.cobox.fragments.feed.adapters.diffutil

import android.support.v7.util.DiffUtil
import com.example.gilgoldzweig.cobox.models.news.NewsHolder

class NewsDiffCallback(private val currentNewsHolder: NewsHolder,
                       private val newsHolder: NewsHolder) : DiffUtil.Callback() {

    override fun getOldListSize(): Int =
            currentNewsHolder.size

    override fun getNewListSize(): Int =
            newsHolder.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        currentNewsHolder[oldItemPosition].title == newsHolder[newItemPosition].title


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        currentNewsHolder[oldItemPosition] == newsHolder[newItemPosition]
}

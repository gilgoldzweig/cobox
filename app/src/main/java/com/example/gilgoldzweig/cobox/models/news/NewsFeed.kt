package com.example.gilgoldzweig.cobox.models.news

import me.toptas.rssconverter.RssFeed
import me.toptas.rssconverter.RssItem


sealed class NewsFeed

data class BusinessNewsFeed(val newsFeed: RssFeed): NewsFeed()

data class EntertainmentNewsFeed(val newsFeed: RssFeed): NewsFeed()

data class EnvironmentNewsFeed(val newsFeed: RssFeed): NewsFeed()

data class NewsHolder(var entertainmentNews: List<RssItem> = emptyList(),
                      var environmentNews: List<RssItem> = emptyList(),
                      var businessNews: List<RssItem> = emptyList(),
                      var showingBusinessNewsFeed: Boolean = true) : NewsFeed() {

    operator fun get(position: Int): RssItem {
        val entertainmentNewsSize = entertainmentNews.size
        return when {
            showingBusinessNewsFeed ->
                businessNews[position]

            position < entertainmentNewsSize ->
                entertainmentNews[position]

            else ->
                environmentNews[position - entertainmentNewsSize]
        }
    }

    val size: Int
        get() = if (showingBusinessNewsFeed) {
            businessNews.size
        } else {
            entertainmentNews.size + environmentNews.size
        }
}


enum class NewsType {
    BUSINESS,
    ENTERTAINMENT,
    ENVIRONMENT
}

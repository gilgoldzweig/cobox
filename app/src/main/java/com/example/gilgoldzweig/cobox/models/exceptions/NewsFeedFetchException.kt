package com.example.gilgoldzweig.cobox.models.exceptions

import com.example.gilgoldzweig.cobox.models.news.NewsType
import java.lang.Exception

class NewsFeedFetchException(val newsType: NewsType, cause: Throwable) : Exception(cause)
package com.example.gilgoldzweig.cobox.timber.utils

import android.os.Build
import com.example.gilgoldzweig.cobox.timber.Timber
import com.nyx.tech.timber.analytics.EventableParam
import com.nyx.tech.timber.analytics.EventableType
import com.nyx.tech.timber.utils.Bundlify
import java.io.PrintWriter
import java.io.StringWriter
import java.util.regex.Pattern


@Suppress("unused")
fun Any?.d(prefix: String = "",
           postfix: String = "",
           separator: String = " "): Unit = Timber.tag(getTag()).d("$prefix$separator${this ?: "null"}$separator$postfix")

@Suppress("unused")
fun Any?.e(prefix: String = "",
           postfix: String = "",
           separator: String = " "): Unit = Timber.tag(getTag()).e("$prefix$separator${this ?: "null"}$separator$postfix")

@Suppress("unused")
fun Any?.i(prefix: String = "",
           postfix: String = "",
           separator: String = " "): Unit = Timber.tag(getTag()).i("$prefix$separator${this ?: "null"}$separator$postfix")

@Suppress("unused")
fun Any?.wtf(prefix: String = "",
             postfix: String = "",
             separator: String = " "): Unit = Timber.tag(getTag()).wtf("$prefix$separator${this ?: "null"}$separator$postfix")

@Suppress("unused")
fun Any?.w(prefix: String = "",
           postfix: String = "",
           separator: String = " "): Unit = Timber.tag(getTag()).w("$prefix$separator${this ?: "null"}$separator$postfix")


@Suppress("unused")
fun EventableType.event(): Unit = Timber.event(this)

@Suppress("unused")
fun Throwable.crash(message: String? = null) {
    if (message == null) {
        Timber.tag(getTag()).crash(this)
    } else {
        Timber.tag(getTag()).crash(this, message)
    }
}


fun EventableType.event(vararg params: Pair<EventableParam, Any>) {
    val bundlify = Bundlify()
    params.forEach { bundlify.put(it.first, it.second) }
    Timber.tag(getTag()).event(this, bundlify)
}

private const val MAX_TAG_LENGTH = 23
private const val CALL_STACK_INDEX = 4
private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

/**
 * Extract the tag which should be used for the message from the `element`. By default
 * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
 * becomes `Foo`).
 *
 *
 * Note: This will not be called if a [manual tag][.tag] was specified.
 */
fun createStackElementTag(element: StackTraceElement): String {
    var tag = element.className
    val m = ANONYMOUS_CLASS.matcher(tag)
    if (m.find()) {
        tag = m.replaceAll("")
    }
    tag = tag.substring(tag.lastIndexOf('.') + 1)
    // Tag length limit was removed in API 24.
    return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        tag
    } else tag.substring(0, MAX_TAG_LENGTH)
}

fun getTag(): String {
    val stackTrace = Throwable().stackTrace
    if (stackTrace.size <= CALL_STACK_INDEX) {
        if (stackTrace.isNotEmpty()) {
            return createStackElementTag(stackTrace.last())
        }
    }
    return createStackElementTag(stackTrace[CALL_STACK_INDEX])
}

fun Throwable.stringify(): String {
    val stringWriter = StringWriter()
    val printWriter = PrintWriter(stringWriter, true)
    printStackTrace(printWriter)
    return stringWriter.buffer.toString()
}
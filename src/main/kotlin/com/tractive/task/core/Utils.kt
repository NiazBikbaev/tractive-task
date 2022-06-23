package com.tractive.task.core

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

fun <T> T.hide(maxElements: Int = 0) = hideIfNotTrace(this, maxElements)

private fun hideIfNotTrace(data: Any?, maxElements: Int = 0): String {
    require(maxElements >= 0) { "maxElements must be zero or positive" }

    if (data == null) return "null"

    if (logger.isTraceEnabled) return data.toString()

    return when (data) {
        is Collection<*> -> {
            return if (data.size <= maxElements) {
                data.toString()
            } else {
                data.joinToString(
                    prefix = "[", postfix = "]", limit = maxElements, truncated = suffix(data.size, maxElements)
                )
            }
        }
        is Map<*, *> -> {
            return if (data.size <= maxElements) {
                data.toString()
            } else {
                data.entries.joinToString(
                    prefix = "{", postfix = "}", limit = maxElements, truncated = suffix(data.size, maxElements)
                )
            }
        }
        is String -> {
            return when {
                data.length <= maxElements -> data
                maxElements != 0 -> "${data.substring(0 until maxElements)}${suffix(data.length, maxElements, "chars")}"
                else -> "***"
            }
        }
        else -> "***"
    }
}

private fun suffix(total: Int, maxElements: Int, elementNames: String = "elements"): String {
    return if (maxElements == 0) {
        "hidden, size=$total"
    } else {
        ".. ${total - maxElements} $elementNames hidden"
    }
}

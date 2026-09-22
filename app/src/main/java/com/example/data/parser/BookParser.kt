package com.example.data.parser

import android.content.Context
import android.net.Uri
import com.example.data.model.Chapter
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.zip.ZipInputStream

object BookParser {

    data class ParsedBook(
        val title: String,
        val author: String,
        val chapters: List<Chapter>,
        val coverColorHex: String = "#854D0E",
        val format: String = "TXT"
    )

    fun chaptersToJson(chapters: List<Chapter>): String {
        val array = JSONArray()
        for (chapter in chapters) {
            val obj = JSONObject()
            obj.put("title", chapter.title)
            obj.put("content", chapter.content)
            array.put(obj)
        }
        return array.toString()
    }

    fun jsonToChapters(jsonStr: String): List<Chapter> {
        if (jsonStr.isBlank()) return emptyList()
        val list = mutableListOf<Chapter>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val title = obj.optString("title", "Chapter ${i + 1}")
                val content = obj.optString("content", "")
                list.add(Chapter(title = title, content = content))
            }
        } catch (_: Exception) {
            // fallback
        }
        return list
    }

    fun parseUri(context: Context, uri: Uri, fileName: String?): ParsedBook {
        val extension = (fileName ?: uri.lastPathSegment ?: "").substringAfterLast('.', "").lowercase()
        val stream = context.contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("Cannot open file stream")

        return stream.use { isStream ->
            when (extension) {
                "epub" -> parseEpub(isStream, fileName)
                "txt" -> parsePlainText(isStream, fileName)
                else -> parsePlainText(isStream, fileName)
            }
        }
    }

    fun parsePlainText(inputStream: InputStream, fallbackTitle: String?): ParsedBook {
        val text = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).readText()
        val cleanName = (fallbackTitle ?: "Untitled Book")
            .substringBeforeLast('.')
            .replace('_', ' ')
            .replace('-', ' ')
            .trim()

        val chapters = splitIntoChapters(text, cleanName)
        return ParsedBook(
            title = cleanName,
            author = "Unknown Author",
            chapters = chapters,
            coverColorHex = pickColorForTitle(cleanName),
            format = "TXT"
        )
    }

    fun parseEpub(inputStream: InputStream, fallbackTitle: String?): ParsedBook {
        var bookTitle = fallbackTitle?.substringBeforeLast('.')?.replace('_', ' ') ?: "Imported eBook"
        var bookAuthor = "Unknown Author"
        val htmlSegments = mutableListOf<Pair<String, String>>() // filename, html content

        try {
            val zis = ZipInputStream(inputStream)
            var entry = zis.nextEntry
            while (entry != null) {
                val name = entry.name.lowercase()
                if (name.endsWith(".opf")) {
                    val opfContent = BufferedReader(InputStreamReader(zis, Charsets.UTF_8)).readText()
                    val titleMatch = "<dc:title[^>]*>(.*?)</dc:title>".toRegex(RegexOption.IGNORE_CASE).find(opfContent)
                    if (titleMatch != null) {
                        bookTitle = cleanHtml(titleMatch.groupValues[1]).trim()
                    }
                    val authorMatch = "<dc:creator[^>]*>(.*?)</dc:creator>".toRegex(RegexOption.IGNORE_CASE).find(opfContent)
                    if (authorMatch != null) {
                        bookAuthor = cleanHtml(authorMatch.groupValues[1]).trim()
                    }
                } else if (name.endsWith(".xhtml") || name.endsWith(".html") || name.endsWith(".htm")) {
                    if (!name.contains("toc") && !name.contains("nav")) {
                        val htmlContent = BufferedReader(InputStreamReader(zis, Charsets.UTF_8)).readText()
                        htmlSegments.add(entry.name to htmlContent)
                    }
                }
                entry = zis.nextEntry
            }
        } catch (_: Exception) {
            // In case of zip parsing quirks, proceed with collected segments
        }

        val chapters = mutableListOf<Chapter>()
        if (htmlSegments.isNotEmpty()) {
            for ((index, item) in htmlSegments.withIndex()) {
                val rawHtml = item.second
                val chapterTitle = extractHeading(rawHtml) ?: "Chapter ${index + 1}"
                val text = cleanHtml(rawHtml)
                if (text.isNotBlank() && text.length > 80) {
                    chapters.add(Chapter(title = chapterTitle, content = text))
                }
            }
        }

        if (chapters.isEmpty()) {
            chapters.add(Chapter(title = "Chapter 1", content = "Imported eBook content."))
        }

        return ParsedBook(
            title = bookTitle,
            author = bookAuthor,
            chapters = chapters,
            coverColorHex = pickColorForTitle(bookTitle),
            format = "EPUB"
        )
    }

    private fun extractHeading(html: String): String? {
        val hRegex = "<h[1-3][^>]*>(.*?)</h[1-3]>".toRegex(RegexOption.IGNORE_CASE)
        val match = hRegex.find(html)
        return match?.let { cleanHtml(it.groupValues[1]).trim() }
    }

    fun cleanHtml(html: String): String {
        return html
            .replace("<style[^>]*>[\\s\\S]*?</style>".toRegex(RegexOption.IGNORE_CASE), "")
            .replace("<script[^>]*>[\\s\\S]*?</script>".toRegex(RegexOption.IGNORE_CASE), "")
            .replace("<br\\s*/?>".toRegex(RegexOption.IGNORE_CASE), "\n")
            .replace("</p>".toRegex(RegexOption.IGNORE_CASE), "\n\n")
            .replace("</div>".toRegex(RegexOption.IGNORE_CASE), "\n")
            .replace("<[^>]+>".toRegex(), "")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&ldquo;", "\"")
            .replace("&rdquo;", "\"")
            .replace("&lsquo;", "'")
            .replace("&rsquo;", "'")
            .replace("&mdash;", "—")
            .replace("&ndash;", "–")
            .replace("\r", "")
            .replace("\n{3,}".toRegex(), "\n\n")
            .trim()
    }

    private fun splitIntoChapters(fullText: String, defaultTitle: String): List<Chapter> {
        val chapterPattern = "(?m)^(?:CHAPTER|Chapter|ACT|Act|PART|Part|Section)\\s+([0-9IVXLCDM]+.*)$".toRegex()
        val matches = chapterPattern.findAll(fullText).toList()

        if (matches.size >= 2) {
            val list = mutableListOf<Chapter>()
            for (i in matches.indices) {
                val start = matches[i].range.first
                val end = if (i < matches.size - 1) matches[i + 1].range.first else fullText.length
                val title = matches[i].value.trim()
                val body = fullText.substring(start + matches[i].value.length, end).trim()
                if (body.isNotBlank()) {
                    list.add(Chapter(title = title, content = body))
                }
            }
            if (list.isNotEmpty()) return list
        }

        // Otherwise chunk into sensible 1,500 word chapters
        val words = fullText.split("\\s+".toRegex()).filter { it.isNotBlank() }
        if (words.size <= 1200) {
            return listOf(Chapter(title = defaultTitle, content = fullText))
        }

        val chapters = mutableListOf<Chapter>()
        val chunkSize = 1200
        var chNum = 1
        for (i in words.indices step chunkSize) {
            val chunk = words.subList(i, minOf(i + chunkSize, words.size)).joinToString(" ")
            chapters.add(Chapter(title = "Chapter $chNum", content = chunk))
            chNum++
        }
        return chapters
    }

    private fun pickColorForTitle(title: String): String {
        val colors = listOf(
            "#9A3412", // Terracotta
            "#1E3A8A", // Oxford Navy
            "#065F46", // Forest Emerald
            "#701A75", // Royal Plum
            "#854D0E", // Warm Amber
            "#991B1B", // Deep Crimson
            "#1F2937", // Slate Charcoal
            "#374151"  // Steel Dark
        )
        val hash = title.hashCode()
        val index = kotlin.math.abs(hash) % colors.size
        return colors[index]
    }
}

package com.example.data.repository

import com.example.data.local.AnnotationDao
import com.example.data.local.AnnotationEntity
import com.example.data.local.BookDao
import com.example.data.local.BookEntity
import com.example.data.local.BookmarkDao
import com.example.data.local.BookmarkEntity
import com.example.data.local.UserStatsDao
import com.example.data.local.UserStatsEntity
import com.example.data.sample.SampleBooks
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookRepository(
    private val bookDao: BookDao,
    private val bookmarkDao: BookmarkDao,
    private val annotationDao: AnnotationDao,
    private val userStatsDao: UserStatsDao
) {
    val allBooks: Flow<List<BookEntity>> = bookDao.getAllBooks()

    fun getBook(id: Long): Flow<BookEntity?> = bookDao.getBookByIdFlow(id)

    suspend fun getBookDirect(id: Long): BookEntity? = bookDao.getBookById(id)

    suspend fun insertBook(book: BookEntity): Long = bookDao.insertBook(book)

    suspend fun updateBook(book: BookEntity) = bookDao.updateBook(book)

    suspend fun deleteBook(id: Long) {
        bookDao.deleteBookById(id)
    }

    suspend fun updateProgress(bookId: Long, chapterIndex: Int, page: Int, percent: Float) {
        bookDao.updateProgress(bookId, chapterIndex, page, percent)
    }

    suspend fun updateFinished(bookId: Long, finished: Boolean) {
        bookDao.updateFinished(bookId, finished)
        if (finished) {
            recordBookCompleted()
        }
    }

    // Bookmarks
    fun getBookmarks(bookId: Long): Flow<List<BookmarkEntity>> = bookmarkDao.getBookmarksForBook(bookId)

    suspend fun addBookmark(bookmark: BookmarkEntity): Long = bookmarkDao.insertBookmark(bookmark)

    suspend fun deleteBookmark(id: Long) = bookmarkDao.deleteBookmarkById(id)

    fun hasBookmarkAt(bookId: Long, chapterIndex: Int, pageIndex: Int): Flow<Boolean> =
        bookmarkDao.hasBookmarkAt(bookId, chapterIndex, pageIndex)

    // Annotations
    fun getAnnotations(bookId: Long): Flow<List<AnnotationEntity>> = annotationDao.getAnnotationsForBook(bookId)

    suspend fun addAnnotation(annotation: AnnotationEntity): Long = annotationDao.insertAnnotation(annotation)

    suspend fun deleteAnnotation(id: Long) = annotationDao.deleteAnnotationById(id)

    // Stats
    fun getUserStats(): Flow<UserStatsEntity?> = userStatsDao.getStats()

    suspend fun recordReadingSession(pagesRead: Int, minutesRead: Int) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        // In a real flow, update streak & pages
        val current = userStatsDao.getStats()
        // If not initialized, insert baseline
    }

    private suspend fun recordBookCompleted() {
        // Increment booksRead
    }

    suspend fun initSampleDataIfNeeded() {
        val count = bookDao.getBookCount()
        if (count == 0) {
            val samples = SampleBooks.getInitialBooks()
            for (book in samples) {
                val insertedId = bookDao.insertBook(book)
                if (insertedId == 1L) {
                    // Add sample bookmark and annotations for Pride & Prejudice
                    bookmarkDao.insertBookmark(
                        BookmarkEntity(
                            bookId = insertedId,
                            chapterIndex = 0,
                            pageIndex = 1,
                            chapterTitle = "Chapter 1 — The Netherfield News",
                            previewSnippet = "\"It is a truth universally acknowledged, that a single man in possession of a good fortune...\""
                        )
                    )
                    annotationDao.insertAnnotation(
                        AnnotationEntity(
                            bookId = insertedId,
                            chapterIndex = 0,
                            selectedText = "It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.",
                            note = "One of the most celebrated opening lines in English literature — ironic and sharp.",
                            colorHex = "#FDE047"
                        )
                    )
                    annotationDao.insertAnnotation(
                        AnnotationEntity(
                            bookId = insertedId,
                            chapterIndex = 1,
                            selectedText = "She is tolerable, but not handsome enough to tempt me.",
                            note = "Darcy's initial arrogance at the assembly ball that sparks the entire conflict.",
                            colorHex = "#FDA4AF"
                        )
                    )
                }
            }

            userStatsDao.insertStats(
                UserStatsEntity(
                    id = 1,
                    booksRead = 4,
                    totalPagesRead = 680,
                    totalMinutesRead = 420,
                    currentStreakDays = 7,
                    lastReadingDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                )
            )
        }
    }
}

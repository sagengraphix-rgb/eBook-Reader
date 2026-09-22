package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AnnotationEntity
import com.example.data.local.BookEntity
import com.example.data.local.BookmarkEntity
import com.example.data.model.Chapter
import com.example.data.model.DictionaryEntry
import com.example.data.model.ReaderFont
import com.example.data.model.ReaderLayoutMode
import com.example.data.model.ReaderPreferences
import com.example.data.model.ReaderTheme
import com.example.data.parser.BookParser
import com.example.data.repository.BookRepository
import com.example.data.sample.DictionaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ReaderUiState(
    val book: BookEntity? = null,
    val chapters: List<Chapter> = emptyList(),
    val currentChapterIndex: Int = 0,
    val currentPageIndex: Int = 0, // 0-indexed within current chapter
    val chapterPages: List<String> = emptyList(),
    val isToolbarsVisible: Boolean = true,
    val preferences: ReaderPreferences = ReaderPreferences(),
    val isTocOpen: Boolean = false,
    val isAnnotationsOpen: Boolean = false,
    val isSettingsOpen: Boolean = false,
    val selectedTextForAction: String? = null,
    val dictionaryEntry: DictionaryEntry? = null,
    val isDictionaryOpen: Boolean = false,
    val isAddNoteOpen: Boolean = false,
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val annotations: List<AnnotationEntity> = emptyList(),
    val isCurrentPageBookmarked: Boolean = false,
    val estimatedMinutesLeftInChapter: Int = 5
)

class ReaderViewModel(
    private val bookId: Long,
    private val repository: BookRepository
) : ViewModel() {

    private val _currentChapterIndex = MutableStateFlow(0)
    private val _currentPageIndex = MutableStateFlow(0)
    private val _isToolbarsVisible = MutableStateFlow(true)
    private val _preferences = MutableStateFlow(ReaderPreferences())
    private val _isTocOpen = MutableStateFlow(false)
    private val _isAnnotationsOpen = MutableStateFlow(false)
    private val _isSettingsOpen = MutableStateFlow(false)
    private val _selectedText = MutableStateFlow<String?>(null)
    private val _dictionaryEntry = MutableStateFlow<DictionaryEntry?>(null)
    private val _isDictionaryOpen = MutableStateFlow(false)
    private val _isAddNoteOpen = MutableStateFlow(false)

    private val _chapters = MutableStateFlow<List<Chapter>>(emptyList())
    private val _cachedPages = MutableStateFlow<List<String>>(emptyList())

    val uiState: StateFlow<ReaderUiState> = combine(
        repository.getBook(bookId),
        _chapters,
        _currentChapterIndex,
        _currentPageIndex,
        _cachedPages,
        _isToolbarsVisible,
        _preferences,
        _isTocOpen,
        _isAnnotationsOpen,
        _isSettingsOpen
    ) { params: Array<Any?> ->
        val book = params[0] as? BookEntity
        @Suppress("UNCHECKED_CAST")
        val chapters = params[1] as List<Chapter>
        val chIdx = params[2] as Int
        val pageIdx = params[3] as Int
        @Suppress("UNCHECKED_CAST")
        val pages = params[4] as List<String>
        val toolbars = params[5] as Boolean
        val prefs = params[6] as ReaderPreferences
        val tocOpen = params[7] as Boolean
        val annOpen = params[8] as Boolean
        val setOpen = params[9] as Boolean

        val currentChapter = chapters.getOrNull(chIdx)
        val wordsLeft = if (currentChapter != null) {
            val ratio = if (pages.isNotEmpty()) (pages.size - 1 - pageIdx).toFloat() / pages.size else 1f
            (currentChapter.wordCount * ratio).toInt()
        } else 0
        val minutesLeft = maxOf(1, wordsLeft / 220)

        ReaderUiState(
            book = book,
            chapters = chapters,
            currentChapterIndex = chIdx,
            currentPageIndex = pageIdx,
            chapterPages = pages,
            isToolbarsVisible = toolbars,
            preferences = prefs,
            isTocOpen = tocOpen,
            isAnnotationsOpen = annOpen,
            isSettingsOpen = setOpen,
            selectedTextForAction = _selectedText.value,
            dictionaryEntry = _dictionaryEntry.value,
            isDictionaryOpen = _isDictionaryOpen.value,
            isAddNoteOpen = _isAddNoteOpen.value,
            bookmarks = emptyList(),
            annotations = emptyList(),
            isCurrentPageBookmarked = false,
            estimatedMinutesLeftInChapter = minutesLeft
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReaderUiState()
    )

    private val _bookmarks = MutableStateFlow<List<BookmarkEntity>>(emptyList())
    val bookmarks: StateFlow<List<BookmarkEntity>> = _bookmarks.asStateFlow()

    private val _annotations = MutableStateFlow<List<AnnotationEntity>>(emptyList())
    val annotations: StateFlow<List<AnnotationEntity>> = _annotations.asStateFlow()

    private val _isCurrentPageBookmarked = MutableStateFlow(false)
    val isCurrentPageBookmarked: StateFlow<Boolean> = _isCurrentPageBookmarked.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getBookmarks(bookId).collect { list ->
                _bookmarks.value = list
                updateCurrentBookmarkState(list, _currentChapterIndex.value, _currentPageIndex.value)
            }
        }
        viewModelScope.launch {
            repository.getAnnotations(bookId).collect { list ->
                _annotations.value = list
            }
        }
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            val book = repository.getBookDirect(bookId) ?: return@launch
            val parsedChapters = BookParser.jsonToChapters(book.chaptersJson)
            _chapters.value = parsedChapters
            val initialCh = book.currentChapterIndex.coerceIn(0, maxOf(0, parsedChapters.size - 1))
            _currentChapterIndex.value = initialCh
            paginateChapter(parsedChapters.getOrNull(initialCh)?.content ?: "", _preferences.value.fontSizeSp)
            val initialPage = (book.currentPage - 1).coerceIn(0, maxOf(0, _cachedPages.value.size - 1))
            _currentPageIndex.value = initialPage
            updateCurrentBookmarkState(_bookmarks.value, initialCh, initialPage)
        }
    }

    private fun paginateChapter(content: String, fontSize: Float) {
        if (content.isBlank()) {
            _cachedPages.value = listOf("No content available.")
            return
        }

        // Adjust character capacity roughly inversely to font size
        // Standard phone screen holds ~700-900 chars at 18sp
        val charsPerPage = (800 * (18f / fontSize)).toInt().coerceIn(350, 1500)
        val paragraphs = content.split("\n\n")
        val pages = mutableListOf<String>()
        val currentPageText = StringBuilder()

        for (para in paragraphs) {
            val cleanPara = para.trim()
            if (cleanPara.isEmpty()) continue

            if (currentPageText.length + cleanPara.length > charsPerPage && currentPageText.isNotEmpty()) {
                pages.add(currentPageText.toString().trim())
                currentPageText.clear()
            }

            if (cleanPara.length > charsPerPage) {
                // Split long paragraphs by sentences
                val sentences = cleanPara.split("(?<=[.!?])\\s+".toRegex())
                for (sentence in sentences) {
                    if (currentPageText.length + sentence.length > charsPerPage && currentPageText.isNotEmpty()) {
                        pages.add(currentPageText.toString().trim())
                        currentPageText.clear()
                    }
                    if (currentPageText.isNotEmpty()) currentPageText.append(" ")
                    currentPageText.append(sentence)
                }
            } else {
                if (currentPageText.isNotEmpty()) currentPageText.append("\n\n")
                currentPageText.append(cleanPara)
            }
        }

        if (currentPageText.isNotEmpty()) {
            pages.add(currentPageText.toString().trim())
        }

        if (pages.isEmpty()) {
            pages.add(content)
        }

        _cachedPages.value = pages
    }

    private fun updateCurrentBookmarkState(list: List<BookmarkEntity>, ch: Int, pg: Int) {
        _isCurrentPageBookmarked.value = list.any { it.chapterIndex == ch && it.pageIndex == pg }
    }

    fun toggleToolbars() {
        _isToolbarsVisible.value = !_isToolbarsVisible.value
    }

    fun nextPage() {
        val totalPages = _cachedPages.value.size
        if (_currentPageIndex.value < totalPages - 1) {
            _currentPageIndex.value += 1
            persistProgress()
        } else if (_currentChapterIndex.value < _chapters.value.size - 1) {
            // Next chapter
            jumpToChapter(_currentChapterIndex.value + 1, page = 0)
        }
    }

    fun prevPage() {
        if (_currentPageIndex.value > 0) {
            _currentPageIndex.value -= 1
            persistProgress()
        } else if (_currentChapterIndex.value > 0) {
            // Previous chapter, last page
            val prevCh = _currentChapterIndex.value - 1
            _currentChapterIndex.value = prevCh
            val chContent = _chapters.value.getOrNull(prevCh)?.content ?: ""
            paginateChapter(chContent, _preferences.value.fontSizeSp)
            _currentPageIndex.value = maxOf(0, _cachedPages.value.size - 1)
            persistProgress()
        }
    }

    fun jumpToPage(pageIndex: Int) {
        _currentPageIndex.value = pageIndex.coerceIn(0, maxOf(0, _cachedPages.value.size - 1))
        persistProgress()
    }

    fun jumpToChapter(chapterIndex: Int, page: Int = 0) {
        if (chapterIndex in _chapters.value.indices) {
            _currentChapterIndex.value = chapterIndex
            val chContent = _chapters.value[chapterIndex].content
            paginateChapter(chContent, _preferences.value.fontSizeSp)
            _currentPageIndex.value = page.coerceIn(0, maxOf(0, _cachedPages.value.size - 1))
            _isTocOpen.value = false
            _isAnnotationsOpen.value = false
            persistProgress()
        }
    }

    private fun persistProgress() {
        val chIdx = _currentChapterIndex.value
        val pgIdx = _currentPageIndex.value
        val totalChapters = maxOf(1, _chapters.value.size)
        val totalPagesInChapter = maxOf(1, _cachedPages.value.size)

        val chapterBasePercent = (chIdx.toFloat() / totalChapters) * 100f
        val pageProgressWithinChapter = (pgIdx.toFloat() / totalPagesInChapter) * (100f / totalChapters)
        val totalPercent = (chapterBasePercent + pageProgressWithinChapter).coerceIn(0f, 100f)

        updateCurrentBookmarkState(_bookmarks.value, chIdx, pgIdx)

        viewModelScope.launch {
            repository.updateProgress(
                bookId = bookId,
                chapterIndex = chIdx,
                page = pgIdx + 1,
                percent = totalPercent
            )
        }
    }

    // Bookmarking
    fun toggleBookmark() {
        val chIdx = _currentChapterIndex.value
        val pgIdx = _currentPageIndex.value
        val existing = _bookmarks.value.find { it.chapterIndex == chIdx && it.pageIndex == pgIdx }

        viewModelScope.launch {
            if (existing != null) {
                repository.deleteBookmark(existing.id)
            } else {
                val currentText = _cachedPages.value.getOrNull(pgIdx) ?: ""
                val snippet = if (currentText.length > 90) currentText.take(90) + "…" else currentText
                val chTitle = _chapters.value.getOrNull(chIdx)?.title ?: "Chapter ${chIdx + 1}"
                repository.addBookmark(
                    BookmarkEntity(
                        bookId = bookId,
                        chapterIndex = chIdx,
                        pageIndex = pgIdx,
                        chapterTitle = chTitle,
                        previewSnippet = snippet
                    )
                )
            }
        }
    }

    fun deleteBookmark(id: Long) {
        viewModelScope.launch {
            repository.deleteBookmark(id)
        }
    }

    // Annotations & Notes
    fun onTextSelected(text: String) {
        _selectedText.value = text
    }

    fun clearTextSelection() {
        _selectedText.value = null
    }

    fun addHighlight(colorHex: String) {
        val text = _selectedText.value ?: return
        viewModelScope.launch {
            repository.addAnnotation(
                AnnotationEntity(
                    bookId = bookId,
                    chapterIndex = _currentChapterIndex.value,
                    selectedText = text,
                    note = "",
                    colorHex = colorHex
                )
            )
            _selectedText.value = null
        }
    }

    fun openNoteDialog() {
        _isAddNoteOpen.value = true
    }

    fun closeNoteDialog() {
        _isAddNoteOpen.value = false
    }

    fun saveNote(noteText: String, colorHex: String = "#FDE047") {
        val text = _selectedText.value ?: "Page Bookmark Note"
        viewModelScope.launch {
            repository.addAnnotation(
                AnnotationEntity(
                    bookId = bookId,
                    chapterIndex = _currentChapterIndex.value,
                    selectedText = text,
                    note = noteText,
                    colorHex = colorHex
                )
            )
            _selectedText.value = null
            _isAddNoteOpen.value = false
        }
    }

    fun deleteAnnotation(id: Long) {
        viewModelScope.launch {
            repository.deleteAnnotation(id)
        }
    }

    // Dictionary
    fun lookupWord(word: String) {
        val entry = DictionaryRepository.lookup(word)
        _dictionaryEntry.value = entry
        _isDictionaryOpen.value = true
    }

    fun closeDictionary() {
        _isDictionaryOpen.value = false
        _dictionaryEntry.value = null
    }

    // TOC & Sheets
    fun openToc() { _isTocOpen.value = true }
    fun closeToc() { _isTocOpen.value = false }
    fun openAnnotations() { _isAnnotationsOpen.value = true }
    fun closeAnnotations() { _isAnnotationsOpen.value = false }
    fun openSettings() { _isSettingsOpen.value = true }
    fun closeSettings() { _isSettingsOpen.value = false }

    // Preferences
    fun setTheme(theme: ReaderTheme) {
        _preferences.value = _preferences.value.copy(theme = theme)
    }

    fun setFont(font: ReaderFont) {
        _preferences.value = _preferences.value.copy(font = font)
    }

    fun adjustFontSize(delta: Float) {
        val newSize = (_preferences.value.fontSizeSp + delta).coerceIn(13f, 32f)
        _preferences.value = _preferences.value.copy(fontSizeSp = newSize)
        val currentContent = _chapters.value.getOrNull(_currentChapterIndex.value)?.content ?: ""
        paginateChapter(currentContent, newSize)
        _currentPageIndex.value = _currentPageIndex.value.coerceIn(0, maxOf(0, _cachedPages.value.size - 1))
    }

    fun setLineHeight(multiplier: Float) {
        _preferences.value = _preferences.value.copy(lineHeightMultiplier = multiplier)
    }

    fun setHorizontalMargin(marginDp: Int) {
        _preferences.value = _preferences.value.copy(horizontalMarginDp = marginDp.coerceIn(8, 64))
    }

    fun setVerticalMargin(marginDp: Int) {
        _preferences.value = _preferences.value.copy(verticalMarginDp = marginDp.coerceIn(8, 48))
    }

    fun setLayoutMode(mode: ReaderLayoutMode) {
        _preferences.value = _preferences.value.copy(layoutMode = mode)
    }

    fun setScreenWarmth(warmth: Float) {
        _preferences.value = _preferences.value.copy(screenWarmth = warmth.coerceIn(0f, 0.45f))
    }

    class Factory(
        private val bookId: Long,
        private val repository: BookRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReaderViewModel(bookId, repository) as T
        }
    }
}

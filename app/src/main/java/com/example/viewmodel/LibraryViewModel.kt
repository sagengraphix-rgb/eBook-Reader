package com.example.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.BookEntity
import com.example.data.local.UserStatsEntity
import com.example.data.parser.BookParser
import com.example.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortOption(val title: String) {
    RECENT("Recently Read"),
    TITLE("Title (A-Z)"),
    AUTHOR("Author"),
    DATE_ADDED("Date Added")
}

data class LibraryUiState(
    val books: List<BookEntity> = emptyList(),
    val filteredBooks: List<BookEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val sortOption: SortOption = SortOption.RECENT,
    val isGridView: Boolean = true,
    val userStats: UserStatsEntity? = null,
    val isImporting: Boolean = false,
    val importMessage: String? = null
)

class LibraryViewModel(private val repository: BookRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow("All")
    private val _sortOption = MutableStateFlow(SortOption.RECENT)
    private val _isGridView = MutableStateFlow(true)
    private val _isImporting = MutableStateFlow(false)
    private val _importMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<LibraryUiState> = combine(
        repository.allBooks,
        _searchQuery,
        _selectedFilter,
        _sortOption,
        _isGridView,
        repository.getUserStats(),
        _isImporting,
        _importMessage
    ) { args: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val allBooks = args[0] as List<BookEntity>
        val query = args[1] as String
        val filter = args[2] as String
        val sort = args[3] as SortOption
        val isGrid = args[4] as Boolean
        val stats = args[5] as? UserStatsEntity
        val importing = args[6] as Boolean
        val message = args[7] as? String

        val filtered = allBooks.filter { book ->
            val matchesQuery = query.isBlank() ||
                book.title.contains(query, ignoreCase = true) ||
                book.author.contains(query, ignoreCase = true) ||
                book.tags.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "All" -> true
                "Reading" -> book.progressPercent > 0 && !book.isFinished
                "Completed" -> book.isFinished
                else -> book.tags.contains(filter, ignoreCase = true)
            }

            matchesQuery && matchesFilter
        }.let { list ->
            when (sort) {
                SortOption.RECENT -> list.sortedByDescending { it.lastReadTimestamp }
                SortOption.TITLE -> list.sortedBy { it.title.lowercase() }
                SortOption.AUTHOR -> list.sortedBy { it.author.lowercase() }
                SortOption.DATE_ADDED -> list.sortedByDescending { it.dateAdded }
            }
        }

        LibraryUiState(
            books = allBooks,
            filteredBooks = filtered,
            searchQuery = query,
            selectedFilter = filter,
            sortOption = sort,
            isGridView = isGrid,
            userStats = stats,
            isImporting = importing,
            importMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LibraryUiState()
    )

    init {
        viewModelScope.launch {
            repository.initSampleDataIfNeeded()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelect(filter: String) {
        _selectedFilter.value = filter
    }

    fun onSortSelect(sort: SortOption) {
        _sortOption.value = sort
    }

    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    fun deleteBook(bookId: Long) {
        viewModelScope.launch {
            repository.deleteBook(bookId)
        }
    }

    fun toggleFinished(bookId: Long, currentFinished: Boolean) {
        viewModelScope.launch {
            repository.updateFinished(bookId, !currentFinished)
        }
    }

    fun importBook(context: Context, uri: Uri, fileName: String?) {
        viewModelScope.launch {
            _isImporting.value = true
            try {
                val parsed = BookParser.parseUri(context, uri, fileName)
                val bookEntity = BookEntity(
                    title = parsed.title,
                    author = parsed.author,
                    coverColorHex = parsed.coverColorHex,
                    format = parsed.format,
                    totalChapters = parsed.chapters.size,
                    currentChapterIndex = 0,
                    currentPage = 1,
                    totalPages = maxOf(1, parsed.chapters.sumOf { maxOf(1, it.content.length / 900) }),
                    progressPercent = 0f,
                    lastReadTimestamp = System.currentTimeMillis(),
                    dateAdded = System.currentTimeMillis(),
                    tags = "Imported, ${parsed.format}",
                    contentRaw = "",
                    chaptersJson = BookParser.chaptersToJson(parsed.chapters),
                    isFinished = false,
                    estimatedMinutes = maxOf(5, parsed.chapters.sumOf { it.estimatedMinutes })
                )
                repository.insertBook(bookEntity)
                _importMessage.value = "Successfully imported \"${parsed.title}\""
            } catch (e: Exception) {
                _importMessage.value = "Error importing book: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isImporting.value = false
            }
        }
    }

    fun clearMessage() {
        _importMessage.value = null
    }

    class Factory(private val repository: BookRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LibraryViewModel(repository) as T
        }
    }
}

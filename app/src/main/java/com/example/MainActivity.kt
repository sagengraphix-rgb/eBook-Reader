package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.BookRepository
import com.example.ui.LibraryScreen
import com.example.ui.ReaderScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.LibraryViewModel
import com.example.viewmodel.ReaderViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = AppDatabase.getDatabase(applicationContext)
    val repository = BookRepository(
      bookDao = database.bookDao(),
      bookmarkDao = database.bookmarkDao(),
      annotationDao = database.annotationDao(),
      userStatsDao = database.userStatsDao()
    )

    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          ReadNestApp(repository = repository)
        }
      }
    }
  }
}

@Composable
fun ReadNestApp(repository: BookRepository) {
  var selectedBookId by remember { mutableStateOf<Long?>(null) }
  val libraryViewModel: LibraryViewModel = viewModel(
    factory = LibraryViewModel.Factory(repository)
  )

  Crossfade(targetState = selectedBookId, label = "screen_transition") { bookId ->
    if (bookId == null) {
      LibraryScreen(
        viewModel = libraryViewModel,
        onOpenBook = { id ->
          selectedBookId = id
        }
      )
    } else {
      BackHandler {
        selectedBookId = null
      }

      val readerViewModel: ReaderViewModel = viewModel(
        key = "reader_$bookId",
        factory = ReaderViewModel.Factory(bookId, repository)
      )

      ReaderScreen(
        viewModel = readerViewModel,
        onNavigateBack = {
          selectedBookId = null
        }
      )
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}


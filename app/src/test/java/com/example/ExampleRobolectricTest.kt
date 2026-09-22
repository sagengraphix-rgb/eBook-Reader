package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.parser.BookParser
import com.example.data.sample.DictionaryRepository
import com.example.data.sample.SampleBooks
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ReadNest", appName)
  }

  @Test
  fun `sample books provide valid data and chapters`() {
    val books = SampleBooks.getInitialBooks()
    assertTrue("Should have multiple initial sample books", books.isNotEmpty())

    val firstBook = books.first()
    assertEquals("Pride and Prejudice", firstBook.title)
    assertTrue("Should have chapters", firstBook.totalChapters > 0)

    val chapters = BookParser.jsonToChapters(firstBook.chaptersJson)
    assertTrue("Parsed chapters should not be empty", chapters.isNotEmpty())
    assertTrue("First chapter should have content", chapters[0].content.isNotBlank())
  }

  @Test
  fun `dictionary repository returns definitions`() {
    val entry = DictionaryRepository.lookup("fortune")
    assertEquals("fortune", entry.word)
    assertNotNull(entry.definition)
    assertTrue(entry.definition.isNotBlank())
  }

  @Test
  fun `book parser clean html strips tags correctly`() {
    val html = "<p>It is a <b>truth</b> universally acknowledged.</p>"
    val cleaned = BookParser.cleanHtml(html)
    assertEquals("It is a truth universally acknowledged.", cleaned)
  }

  @Test
  fun `reader preferences default and customized margins and text sizes`() {
    val defaultPrefs = com.example.data.model.ReaderPreferences()
    assertEquals(18f, defaultPrefs.fontSizeSp, 0.01f)
    assertEquals(20, defaultPrefs.horizontalMarginDp)
    assertEquals(16, defaultPrefs.verticalMarginDp)

    val customPrefs = defaultPrefs.copy(
      fontSizeSp = 24f,
      horizontalMarginDp = 36,
      verticalMarginDp = 24
    )
    assertEquals(24f, customPrefs.fontSizeSp, 0.01f)
    assertEquals(36, customPrefs.horizontalMarginDp)
    assertEquals(24, customPrefs.verticalMarginDp)
  }
}


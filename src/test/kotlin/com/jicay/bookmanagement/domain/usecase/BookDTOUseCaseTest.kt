package com.jicay.bookmanagement.domain.usecase

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BookDTOUseCaseTest {

    @InjectMockKs
    private lateinit var bookUseCase: BookUseCase

    @MockK
    private lateinit var bookPort: BookPort

    @Test
    fun `get all books should returns all books sorted by name`() {
        every { bookPort.getAllBooks() } returns listOf(
                Book("Les Misérables", "Victor Hugo"),
                Book("Hamlet", "William Shakespeare")
        )

        val res = bookUseCase.getAllBooks()

        assertThat(res).containsExactly(
                Book("Hamlet", "William Shakespeare", false),
                Book("Les Misérables", "Victor Hugo", false)
        )
    }

    @Test
    fun `add book`() {
        justRun { bookPort.createBook(any()) }

        val book = Book("Les Misérables", "Victor Hugo")

        bookUseCase.addBook(book)

        verify(exactly = 1) { bookPort.createBook(book) }
    }

    @Test
    fun `reserve book`() {
        every { bookPort.getBookByTitle("Les Misérables") } answers { Book("Les Misérables", "Victor Hugo", false) }
        every { bookPort.reserveBook("Les Misérables") } answers { nothing }

        val book = Book("Les Misérables", "Victor Hugo")

        bookUseCase.reserveBook(book.name)

        verify(exactly = 1) { bookPort.reserveBook(book.name) }
    }

    @Test
    fun `error book already reserved`() {
        every { bookPort.getBookByTitle("Les Misérables") } answers { Book("Les Misérables", "Victor Hugo", true) }
        // every { bookPort.reserveBook("Les Misérables") } answers { nothing }

        val book = Book("Les Misérables", "Victor Hugo")

        assertFailure{ bookUseCase.reserveBook(book.name) }
                .isInstanceOf(NoSuchElementException::class.java)
                .hasMessage("Book with title ${book.name} not found or already reserved")
    }
}
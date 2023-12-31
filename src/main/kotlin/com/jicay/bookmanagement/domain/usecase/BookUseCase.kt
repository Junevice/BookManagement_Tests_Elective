package com.jicay.bookmanagement.domain.usecase

import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort

class BookUseCase(
    private val bookPort: BookPort
) {
    fun getAllBooks(): List<Book> {
        return bookPort.getAllBooks().sortedBy {
            it.name.lowercase()
        }
    }

    fun getBookByTitle(title: String) : Book {
        val book = bookPort.getBookByTitle(title)
        if(book!=null){
            return book
        } else{
            throw NoSuchElementException("Book with title $title not found")
        }
    }

    fun addBook(book: Book) {
        bookPort.createBook(book)
    }

    fun reserveBook(title: String) {
        val book = bookPort.getBookByTitle(title)
        if (!book!!.isReserved) {
            bookPort.reserveBook(title)
        }
        else{
            throw Exception("Book with title $title already reserved")
        }
    }
}
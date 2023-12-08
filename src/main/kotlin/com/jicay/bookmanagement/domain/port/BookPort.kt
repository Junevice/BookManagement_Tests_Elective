package com.jicay.bookmanagement.domain.port

import com.jicay.bookmanagement.domain.model.Book

interface BookPort {
    fun getAllBooks(): List<Book>
    fun getBookByTitle(title: String): Book
    fun createBook(book: Book)
    fun reserveBook(title: String)
}
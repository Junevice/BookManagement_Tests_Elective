package com.jicay.bookmanagement.infrastructure.driver.web.dto

import com.jicay.bookmanagement.domain.model.Book

data class BookDTO(val name: String, val author: String, val isReserved: Boolean) {
    fun toDomain(): Book {
        return Book(
            name = this.name,
            author = this.author,
            isReserved = this.isReserved
        )
    }
}

fun Book.toDto() = BookDTO(
    name = this.name,
    author = this.author,
    isReserved = this.isReserved
)
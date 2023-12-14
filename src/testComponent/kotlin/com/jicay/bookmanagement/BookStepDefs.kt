package com.jicay.bookmanagement

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import io.restassured.response.ValidatableResponse
import org.springframework.boot.test.web.server.LocalServerPort

class BookStepDefs {
    @LocalServerPort
    private var port: Int? = 0

    @Before
    fun setup(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @When("the user creates the book {string} written by {string} and is {string}")
    fun createBook(title: String, author: String, isReserved: String) {
        val isReservedBoolean = isReserved.toBoolean()
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                    {
                      "name": "$title",
                      "author": "$author",
                      "is_reserved": $isReservedBoolean
                    }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
            .statusCode(201)
    }

    @When("the user get all books")
    fun getAllBooks() {
        lastBookResult = given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(200)
    }

    @When("the user reserve the book titled {string}")
    fun reserveBook(title: String) {
        given()
                .contentType(ContentType.JSON)
                .and()
                .`when`()
                .put("/books/${title}")
                .then()
                .statusCode(200)
    }

    @When("the user retrieve the book titled {string}")
    fun getBookByTitle(title: String) {
        BookResult = given()
                .`when`()
                .get("/books/${title}")
                .then()
                .statusCode(200)
    }

    @Then("the list should contains the following books in the same order")
    fun shouldHaveListOfBooks(payload: List<Map<String, Any>>) {
        val expectedResponse = payload.joinToString(separator = ",", prefix = "[", postfix = "]") { line ->
            """
                ${
                    line.entries.joinToString(separator = ",", prefix = "{", postfix = "}") {
                        if (it.key == "reserved") {
                            """"${it.key}": ${it.value}"""
                        } else {
                            """"${it.key}": "${it.value}""""
                        }
                    }
                }
            """.trimIndent()

        }
        assertThat(lastBookResult.extract().body().jsonPath().prettify())
            .isEqualTo(JsonPath(expectedResponse).prettify())

    }

    @Then("the book reservation status should be {string}")
    fun bookReservationInfoShouldShowStatus(payload: String) {
        val expectedResponse = payload.toBoolean()
        assertThat(BookResult.extract().body().jsonPath().getBoolean("reserved")).isEqualTo(expectedResponse)
    }

    companion object {
        lateinit var lastBookResult: ValidatableResponse
        lateinit var BookResult: ValidatableResponse
    }
}
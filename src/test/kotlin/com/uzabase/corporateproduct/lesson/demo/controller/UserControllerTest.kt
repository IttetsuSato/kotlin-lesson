package com.uzabase.corporateproduct.lesson.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.jupiter.params.provider.Arguments
import org.slf4j.LoggerFactory
import java.util.stream.Stream

data class Expected(
    val expectedStatus: Int,
    val expectedKey: String? = null,
    val expectedError: String? = null
)

data class TestData(
    val offset: Int? = null,
    val limit: Int? = null,
    val word: String? = null,
    val email: String? = null,
    val expected: Expected
)

@WebMvcTest
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        @JvmStatic
        fun provideSearchRequestParameters(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("offset, limitが指定されている", TestData(offset = 0, limit = 1, word = "validWord", expected = Expected(200))),
                Arguments.of("offsetが0, limitを指定しない", TestData(offset = 0, word = "validWord", expected = Expected(200))),
                Arguments.of("offsetを指定しない, limitが1", TestData(limit = 1, word = "validWord", expected = Expected(200))),
                Arguments.of("offsetが-1", TestData(offset = -1, word = "validWord", expected = Expected(400, "offset", "must be greater than or equal to 0"))),
                Arguments.of("limitが0", TestData(limit = 0, word = "validWord", expected = Expected(400, "limit", "must be greater than or equal to 1"))),
                Arguments.of("wordがアスタリスクを含む", TestData(word = "invalid*Word", expected = Expected(400, "word", "Contains invalid characters: *"))),
                Arguments.of("wordが30文字", TestData(word = "a".repeat(30), expected = Expected(200))),
                Arguments.of("wordが31文字", TestData(word = "a".repeat(31), expected = Expected(400, "word", "size must be between 0 and 30"))),
                Arguments.of("emailが不正なフォーマット", TestData(email = "invalidEmail", expected = Expected(400, "email", "Invalid email format"))),
                Arguments.of("emailが1024文字", TestData(email = "a".repeat(1012) + "@example.com", expected = Expected(200))),
                Arguments.of("emailが1025文字", TestData(email = "a".repeat(1013) + "@example.com", expected = Expected(400, "email", "size must be between 0 and 1024"))),
                Arguments.of("wordとemailが両方指定されている", TestData(word = "validWord", email = "validEmail", expected = Expected(400, "searchRequest", "Either word or email is required"))),
                Arguments.of("wordが空文字", TestData(word = "", expected = Expected(400, "searchRequest", "Either word or email is required"))),
                Arguments.of("emailが空文字", TestData(email = "", expected = Expected(400, "searchRequest", "Either word or email is required")))
            )
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideSearchRequestParameters")
    @DisplayName("Validation tests for /users/search")
    fun validateSearchRequest(testTitle: String, testData: TestData) {
        val requestMap = mapOf("offset" to testData.offset, "limit" to testData.limit, "word" to testData.word, "email" to testData.email)
        val expected = testData.expected
        val logger = LoggerFactory.getLogger(javaClass)
        logger.debug("requestJson: {}", requestMap)

        val requestJson = objectMapper.writeValueAsString(requestMap)
        val resultActions = mockMvc.perform(post("/users/search").contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andExpect(status().`is`(expected.expectedStatus))

        if (expected.expectedError != null) {
            resultActions.andExpect(jsonPath("$.${expected.expectedKey}").value(expected.expectedError))
        }
    }
}
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

data class TestData(
    val offset: Int,
    val limit: Int,
    val word: String,
    val email: String,
    val expectedStatus: Int,
    val expectedKey: String? = null,
    val expectedError: String? = null
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
                Arguments.of("正常系（offsetが0, limitが1, wordが適切な文字列）", TestData( 0, 1, "validWord", "", 200)),
                Arguments.of("offsetが-1", TestData(-1, 1, "validWord", "", 400, "offset", "must be greater than or equal to 0")),
                Arguments.of("limitが0", TestData(0, 0, "validWord", "", 400, "limit", "must be greater than or equal to 1")),
                Arguments.of("wordがアスタリスクを含む", TestData(0, 1, "invalid*word", "", 400, "word", "Contains invalid characters: *")),
                Arguments.of("wordが30文字", TestData(0, 1, "", "", 400)),
                Arguments.of("wordが31文字", TestData(0, 1, "a".repeat(31), "", 400, "word", "size must be between 0 and 30")),
                Arguments.of("emailが1024文字", TestData(0, 1, "", "a".repeat(1024), 200)),
                Arguments.of("emailが1025文字", TestData(0, 1, "", "a".repeat(1025), 400, "email", "size must be between 0 and 1024")),
                Arguments.of("wordとemailが両方指定されている", TestData(0, 1, "validWord", "validEmail", 400 )),
            )
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideSearchRequestParameters")
    @DisplayName("Validation tests for /users/search")
    fun validateSearchRequest(testTitle: String, testData: TestData) {
        val requestMap = mapOf("offset" to testData.offset, "limit" to testData.limit, "word" to testData.word, "email" to testData.email)
        val logger = LoggerFactory.getLogger(javaClass)
        logger.debug("requestJson: {}", requestMap)

        val requestJson = objectMapper.writeValueAsString(requestMap)
        val resultActions = mockMvc.perform(post("/users/search").contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andExpect(status().`is`(testData.expectedStatus))

        if (testData.expectedError != null) {
            resultActions.andExpect(jsonPath("$.${testData.expectedKey}").value(testData.expectedError))
        }
    }
}
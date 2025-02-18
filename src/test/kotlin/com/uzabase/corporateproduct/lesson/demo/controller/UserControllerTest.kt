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
    val title: String,
    val offset: Int,
    val limit: Int,
    val word: String,
    val email: String,
    val expectedStatus: Int,
    val expectedError: String?
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
                Arguments.of("正常系（offsetが0, limitが1, wordが適切な文字列）", 0, 1, "validWord", "", 200, null),
                Arguments.of("offsetが-1", -1, 1, "validWord", "", 400, "マイナスは不可です"),
                Arguments.of("limitが0", 0, 0, "validWord", "", 400, "must be greater than or equal to 1"),
                Arguments.of("wordがアスタリスクを含む", 0, 1, "invalid*word", "", 400, "アスタリスクを含むワードは指定できません")
            )
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideSearchRequestParameters")
    @DisplayName("Validation tests for /users/search")
    fun validateSearchRequest(testTitle: String, offset: Int, limit: Int, word: String, email: String, expectedStatus: Int, expectedError: String?) {
        val requestMap = mapOf("offset" to offset, "limit" to limit, "word" to word, "email" to email)
        val logger = LoggerFactory.getLogger(javaClass)
        logger.debug("requestJson: {}", requestJson)

        val requestJson = objectMapper.writeValueAsString(requestMap)
        val resultActions = mockMvc.perform(post("/users/search").contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andExpect(status().`is`(expectedStatus))

        if (expectedError != null) {
            resultActions.andExpect(jsonPath("$.offset").value(expectedError))
        }

    }
}
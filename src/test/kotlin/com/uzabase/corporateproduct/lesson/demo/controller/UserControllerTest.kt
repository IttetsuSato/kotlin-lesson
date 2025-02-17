package com.uzabase.corporateproduct.lesson.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("/users/searchのoffsetは0以上でなければエラー")
    fun offset_must_be_greater_than_0() {
        val requestMap = mapOf("offset" to "-1")
        val requestJson = objectMapper.writeValueAsString(requestMap)
        mockMvc.perform(post("/users/search").contentType(MediaType.APPLICATION_JSON).content(requestJson))
            .andExpect(status().isBadRequest) // HTTPステータスコードが400
            .andExpect(jsonPath("$.offset").value("マイナスは不可です")) // JSONの `offset` キーを検証
    }
}
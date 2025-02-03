package com.uzabase.corporateproduct.lesson.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

data class User(val id: UUID, val name: String)

@RestController
class UserController {
    @GetMapping("/users/{userId}/workHistories/{workHistoryId}")
    fun getUserById(@PathVariable pathVarsMap: Map<String, String>, @RequestParam("year") year: String, @RequestParam("sort", required = false, defaultValue = "asc") sort: String?): ResponseEntity<User> {
        val userId = pathVarsMap["userId"]
        val workHistoryId = pathVarsMap["workHistoryId"]
        if (userId == null || workHistoryId == null) {
            return ResponseEntity.badRequest().build()
        }
        val logger = LoggerFactory.getLogger(javaClass)
        logger.debug("userId: {}, workHistoryId: {}, year: {}, sort: {}", userId, workHistoryId, year, sort)
        return ResponseEntity.accepted().build()
    }
}

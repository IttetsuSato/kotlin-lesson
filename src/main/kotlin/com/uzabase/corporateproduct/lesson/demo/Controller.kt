package com.uzabase.corporateproduct.lesson.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

data class User(val id: UUID, val name: String)

@RestController
class UserController {
    @RequestMapping("/users/{userId}/results/{resultId}", method = [RequestMethod.GET])
    fun getUserById(@PathVariable userId: String, @PathVariable resultId: String, @RequestParam("option") option: String, @RequestParam("option2", required = false) option2: String?): ResponseEntity<User> {
        val logger = LoggerFactory.getLogger(javaClass)
        logger.debug("userId: {}, resultId: {}, option: {}, option2: {}", userId, resultId, option, option2)
        return ResponseEntity.accepted().build()
    }
}
package com.uzabase.corporateproduct.lesson.demo.controller

// jakartaってなんだろう
import com.uzabase.corporateproduct.lesson.demo.validator.StartBeforeEnd
import com.uzabase.corporateproduct.lesson.demo.validator.ValidCharacters
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

data class User(val id: UUID, val name: String)

@StartBeforeEnd
data class TimeRangeRequest(
    val start: Long,
    val end: Long
)

data class SearchRequest(
    @field:ValidCharacters(ngChars = "*", message = "アスタリスクを含むワードは指定できません")
    val word: String?,
    @field:Min(0, message = "マイナスは不可です")
    val offset: Int?,
    @field:Min(1)
    val limit: Int?
)

data class SearchResult(val users: List<User>)

@RestController
class UserController {
    @GetMapping("/users/{userId}/workHistories/{workHistoryId}")
    fun getUserById(
        @PathVariable userId: String,
        @PathVariable workHistoryId: String,
        @RequestParam("year") year: String,
        @RequestParam("sort", required = false, defaultValue = "asc") sort: String?
    ): ResponseEntity<User> {
        val logger = LoggerFactory.getLogger(javaClass)
        logger.debug("userId: {}, workHistoryId: {}, year: {}, sort: {}", userId, workHistoryId, year, sort)
        return ResponseEntity.accepted().build()
    }

    @RequestMapping("/users/search", method = [RequestMethod.POST])
    fun searchUsers(@Valid @RequestBody body: SearchRequest): ResponseEntity<SearchResult> {
        val logger = LoggerFactory.getLogger(javaClass)
        logger.debug("body: {}", body)
        return ResponseEntity.ok(SearchResult(Collections.emptyList()))
    }
}

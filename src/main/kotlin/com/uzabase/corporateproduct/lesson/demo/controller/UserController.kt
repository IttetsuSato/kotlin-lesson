package com.uzabase.corporateproduct.lesson.demo.controller

// jakartaってなんだろう
import com.uzabase.corporateproduct.lesson.demo.validator.StartBeforeEnd
import com.uzabase.corporateproduct.lesson.demo.validator.ValidCharacters
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

data class User(val id: UUID, val name: String)
///users/searchハンドラを改造します
//SearchRequestクラスに、新たにemailという、Null許可の文字列プロパティを追加します
//emailプロパティは、「メールアドレスであること」を検証する必要があります。
//wordとemailは、必ずどちらかが指定されている必要があります。
//wordとemailは、どちらかしか指定できません（両方入っているのはNG）
//wordの長さの上限は30文字です
//emailの長さの上限は1024文字です
//wordとemailどちらを指定するにせよ、空文字はNGです
//v offsetの下限は0です
//v limitの下限は1です。
//受け取ったすべてのパラメータを、ログに出力してください。
//offsetが指定されていない場合は、offsetは0とみなします
//limitが指定されていない場合は、limitは20とみなします
//すべてのバリデーション定義について、自動テストを実装してください
//入力値に範囲がある場合は、エラーとなる境界を指定してテストしてください（成功するケースと失敗するケース）
@StartBeforeEnd
data class TimeRangeRequest(
    val start: Long,
    val end: Long
)

data class SearchRequest(
    @field:Min(0)
    val offset: Int?,
    @field:Min(1)
    val limit: Int?,
    @field:ValidCharacters(ngChars = "*")
    val word: String?,
    val email: String?
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

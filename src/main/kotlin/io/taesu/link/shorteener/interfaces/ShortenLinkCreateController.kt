package io.taesu.link.shorteener.interfaces

import io.taesu.link.app.config.API_V1
import io.taesu.link.app.config.AppConstants
import io.taesu.link.base.interfaces.SuccessResponse
import io.taesu.link.shorteener.application.ShortenLinkCreateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@RestController
class ShortenLinkCreateController(
        val service: ShortenLinkCreateService,
        val constants: AppConstants
) {

    @PostMapping(value = ["$API_V1/links"], produces = ["application/json"])
    fun create(@RequestBody request: ShortenLinkCreateRequest): ResponseEntity<SuccessResponse> {
        return SuccessResponse.created(service.create(request, constants.domainUrl))
    }
}

data class ShortenLinkCreateRequest(val link: String,
                                    val expirationDuration: Duration? = null)

data class ShortenLinkCreateResponse(
        val key: Long,
        val originLink: String,
        val shortenLink: String,
        val expiredAt: String = ""
)
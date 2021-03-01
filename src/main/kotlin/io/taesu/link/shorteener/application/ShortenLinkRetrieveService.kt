package io.taesu.link.shorteener.application

import io.taesu.link.base.exception.UnCaughtableException.Companion.doThrow
import io.taesu.link.shorteener.domain.ShortenLink
import io.taesu.link.shorteener.domain.ShortenLinkRepository
import io.taesu.link.app.config.LocalDateTimeProvider
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ShortenLinkRetrieveService(
        private val shortenLinkRepository: ShortenLinkRepository
) {
    fun retrieveOriginLink(hash: String, nowProvider: LocalDateTimeProvider): String {
        return with(retrieveShortenLink(hash)) {
            if (isExpired(nowProvider.invoke())) {
                doThrow(errorCode = "EXPIRED", message = "[$hash] is expired at ${expiredAt.toString()}")
            }
            originLink
        }
    }

    fun retrieveShortenLink(hash: String): ShortenLink = with(shortenLinkRepository) {
        findByHash(hash) ?: doThrow(errorCode = "HASH_NOT_FOUND", message = "[$hash] is invalid key of ShortenLink")
    }
}

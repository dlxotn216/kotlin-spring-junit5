package io.taesu.link.shorteener.application

import io.taesu.link.app.config.initialSequence
import io.taesu.link.app.utils.response
import io.taesu.link.app.utils.yyyyMMddhhmmss
import io.taesu.link.base.exception.UnCaughtableException
import io.taesu.link.shorteener.domain.ShortenLink
import io.taesu.link.shorteener.domain.ShortenLinkRepository
import io.taesu.link.shorteener.interfaces.ShortenLinkCreateRequest
import io.taesu.link.shorteener.interfaces.ShortenLinkCreateResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class ShortenLinkCreateService(
        val repository: ShortenLinkRepository,
) {
    companion object {
        private const val allowedString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        private val allowedCharacters = allowedString.toCharArray()
        private val base = allowedCharacters.size
    }

    @Transactional
    fun create(request: ShortenLinkCreateRequest, domainUrl: String): ShortenLinkCreateResponse {
        val shorten = with(request) {
            repository.save(ShortenLink(originLink = link, expirationDuration = expirationDuration))
        }

        return with(shorten) {
            val sequence = key ?: throw UnCaughtableException(errorCode = "NPE", message = "Saved entity key is null")
            hash = getHash(sequence, createdAt)
            ShortenLinkCreateResponse(sequence, originLink, "$domainUrl/${hash}", expiredAt?.response() ?: "")
        }
    }

    fun getHash(sequence: Long, createdAt: LocalDateTime): String {
        val initialHash = encode(sequence)
        if (repository.findByHash(initialHash) == null) {
            return initialHash
        }

        val reHashed = "${initialHash}-${createdAt.yyyyMMddhhmmss()}"
        if (repository.findByHash(reHashed) != null) {
            throw UnCaughtableException(
                    errorCode = "REHASH_FAIL",
                    message = "Rehashed again conflict.")
        }

        return reHashed
    }

    fun encode(input: Long): String {
        var sequence = input + initialSequence
        val encodedString = StringBuilder()
        while (sequence > 0) {
            encodedString.append(allowedCharacters.get((sequence % base).toInt()))
            sequence /= base
        }
        return encodedString.reverse().toString()
    }
}

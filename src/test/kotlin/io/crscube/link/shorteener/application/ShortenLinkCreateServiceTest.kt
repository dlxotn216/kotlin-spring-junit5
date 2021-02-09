package io.crscube.link.shorteener.application

import io.crscube.link.app.config.AppConstants
import io.crscube.link.base.domain.Audit
import io.crscube.link.base.exception.UnCaughtableException
import io.crscube.link.shorteener.domain.ShortenLink
import io.crscube.link.shorteener.domain.ShortenLinkRepository
import io.crscube.link.shorteener.interfaces.ShortenLinkCreateRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@ExtendWith(MockitoExtension::class)
internal class ShortenLinkCreateServiceTest {

    @Mock
    lateinit var repository: ShortenLinkRepository

    @Mock
    lateinit var constants: AppConstants

    @Spy
    @InjectMocks
    lateinit var service: ShortenLinkCreateService

    @Test
    fun `When hash conflict should retry`() {
        // given
        val key = 1L
        val initialHash = "aaaa"
        val createdAt = LocalDateTime.now()
        doReturn(initialHash).`when`(service).encode(key)
        doReturn(ShortenLink(key, "https://localhost:9090/set-password")).`when`(repository).findByHash(initialHash)

        // when
        val hash = service.getHash(key, createdAt)

        // then
        assert(hash != initialHash)
        println(hash)
    }

    @Test
    fun `When hash reconflict should throw`() {
        // given
        val key = 1L
        val initialHash = "aaaa"
        val createdAt = LocalDateTime.now()
        doReturn(initialHash).`when`(service).encode(key)
        doReturn(ShortenLink(key, "https://localhost:9090/set-password")).`when`(repository).findByHash(anyString())

        // then
        assertThrows<UnCaughtableException>(
                ""
        ) { service.getHash(key, createdAt) }
    }

    @Test
    fun `Should success to create shorten link`() {
        // given
        val givenKey = 1L
        val givenHash = "aaaa"
        val givenCreatedAt = LocalDateTime.now()
        val givenLink = "https://localhost:9090/set-password"
        val givenDomainUrl = "http://link.crscube.io"

        doReturn(ShortenLink(givenKey, givenLink, Audit(createdAt = givenCreatedAt))).`when`(repository).save(any())
        doReturn(givenHash).`when`(service).getHash(givenKey, givenCreatedAt)

        // when
        val (key, originLink, shortenLink) = service.create(ShortenLinkCreateRequest(givenLink), givenDomainUrl)

        // then
        assert(originLink == givenLink)
        assert(shortenLink == "$givenDomainUrl/$givenHash")

    }
}
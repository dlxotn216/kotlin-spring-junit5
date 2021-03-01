package io.taesu.link.shorteener.application

import io.taesu.link.base.exception.UnCaughtableException
import io.taesu.link.shorteener.domain.ShortenLink
import io.taesu.link.shorteener.domain.ShortenLinkRepository
import io.taesu.link.app.config.LocalDateTimeProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Duration
import java.time.LocalDateTime

/**
 * Created by itaesu on 2021/02/17.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@ExtendWith(MockitoExtension::class)
internal class ShortenLinkRetrieveServiceTest {
    @Mock
    lateinit var repository: ShortenLinkRepository

    @Spy
    @InjectMocks
    lateinit var service: ShortenLinkRetrieveService

    companion object {
        val hash = "cLssa"
        val expected = "https://localhost:8080/api/v1"

        @JvmStatic
        fun givenNotExpiredLinks() = arrayListOf(
                ShortenLink(1L, expected, hash),                            // 만료가 없는 경우
                ShortenLink(1L, expected, hash, Duration.ofMinutes(60L))    // 먼 미래에 만료가 잇는 경우
        )
    }

    @ParameterizedTest
    @MethodSource("givenNotExpiredLinks")
    fun `Should return originLink`(expiredLink: ShortenLink) {
        // given
        doReturn(expiredLink).`when`(repository).findByHash(hash)

        // when
        val actual = service.retrieveOriginLink(hash, LocalDateTimeProvider())

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Should throw when hash not found`() {
        // given
        val hash = "cLssa"
        doReturn(null).`when`(repository).findByHash(hash)

        // then
        assertThrows<UnCaughtableException> {
            // when
            service.retrieveShortenLink(hash)
        }
    }

    @Test
    fun `Should throw when hash is expired`() {
        // given
        val hash = "cLssa"
        val expiredLink = ShortenLink(1L, "https://localhost:8080/api/v1", hash, Duration.ofMinutes(-1))
        doReturn(expiredLink).`when`(repository).findByHash(hash)

        // then
        assertThrows<UnCaughtableException> {
            // when
            service.retrieveOriginLink(hash, LocalDateTimeProvider())
        }
    }
}
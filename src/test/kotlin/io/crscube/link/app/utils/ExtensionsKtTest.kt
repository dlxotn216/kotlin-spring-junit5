package io.crscube.link.app.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.time.Month

/**
 * Created by itaesu on 2021/02/15.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
internal class ExtensionsKtTest {

    companion object {
        // given
        @JvmStatic
        fun testParameters1() = arrayListOf(
                Arguments.of(
                        LocalDateTime.of(2021, Month.FEBRUARY, 16, 12, 34, 56),
                        "20210216123456"),
                Arguments.of(
                        LocalDateTime.of(2021, Month.MARCH, 16, 2, 4, 0),
                        "20210316020400")
        )

        // given
        @JvmStatic
        fun testParameters2() = arrayListOf(
                Arguments.of(
                        LocalDateTime.of(2021, Month.FEBRUARY, 16, 12, 34, 56),
                        "2021-02-16 12:34:56 UTC"),
                Arguments.of(
                        LocalDateTime.of(2021, Month.DECEMBER, 1, 7, 2, 1),
                        "2021-12-01 07:02:01 UTC"),
        )
    }

    @ParameterizedTest
    @MethodSource("testParameters1")
    fun `yyyyMMddhhmmss datetime format should match`(actual: LocalDateTime, expected: String) {

        // when
        val response = actual.yyyyMMddhhmmss()

        // then
        assertThat(response).isEqualTo(expected)
    }

    @ParameterizedTest
    @MethodSource("testParameters2")
    fun `Response datetime format should match`(actual: LocalDateTime, expected: String) {
        // when
        val response = actual.response()

        // then
        assertThat(response).isEqualTo(expected)
    }
}
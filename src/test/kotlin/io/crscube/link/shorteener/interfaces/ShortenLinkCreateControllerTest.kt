package io.crscube.link.shorteener.interfaces

import io.crscube.link.app.config.API_V1
import io.crscube.link.app.config.AppConstants
import io.crscube.link.shorteener.application.ShortenLinkCreateService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Duration

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@WebMvcTest(ShortenLinkCreateController::class)
internal class ShortenLinkCreateControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var appConstants: AppConstants

    @MockBean
    lateinit var service: ShortenLinkCreateService

    @Test
    fun `Should return without expiration ShortenLinkRetrieveResponse`() {
        // given
        val givenLink = "https://localhost/set-password"
        val domainUrl = "https://link.crscube.io"
        val givenShortenLink = "$domainUrl/aefaf"
        val request = ShortenLinkCreateRequest(givenLink)

        doReturn(domainUrl).`when`(appConstants).domainUrl
        `when`(service.create(request, domainUrl)).thenReturn(ShortenLinkCreateResponse(1L, givenLink, givenShortenLink))

        // when
        val post =
                post("$API_V1/links")
                        .accept("application/json")
                        .contentType("application/json")
                        .content("""
                            {
                              "link": "$givenLink"
                            }
                        """.trimIndent())

        val perform = this.mockMvc.perform(post)

        // then
        perform.andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.shortenLink").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.expiredAt").value(""))
                .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `Should return with expiration ShortenLinkRetrieveResponse`() {
        // given
        val givenLink = "https://localhost/set-password"
        val domainUrl = "https://link.crscube.io"
        val givenShortenLink = "$domainUrl/aefaf"
        val expirationDuration = "PT60M"    // 60 minutes
        val request = ShortenLinkCreateRequest(givenLink, Duration.parse(expirationDuration))

        doReturn(domainUrl).`when`(appConstants).domainUrl
        `when`(service.create(request, domainUrl))
                .thenReturn(ShortenLinkCreateResponse(1L, givenLink, givenShortenLink, "2021-02-12 12:34:56 UTC"))

        // when
        val post =
                post("$API_V1/links")
                        .accept("application/json")
                        .contentType("application/json")
                        .content("""
                            {
                              "link": "$givenLink",
                              "expirationDuration": "$expirationDuration"
                            }
                        """.trimIndent())

        val perform = this.mockMvc.perform(post)

        // then
        perform.andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.shortenLink").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.expiredAt").isNotEmpty)
                .andDo(MockMvcResultHandlers.print())

    }
}
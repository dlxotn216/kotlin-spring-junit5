package io.taesu.link.shorteener.interfaces

import io.taesu.link.shorteener.application.ShortenLinkRetrieveService
import io.taesu.link.app.config.LocalDateTimeProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import java.time.LocalDateTime

/**
 * Created by itaesu on 2021/02/17.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(ShortenLinkRedirectController::class)
internal class ShortenLinkRedirectControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var provider: LocalDateTimeProvider

    @MockBean
    lateinit var service: ShortenLinkRetrieveService

    @Test
    fun `Should redirect to origin link`() {
        // given
        val hash = "cLs"
        val originLink = "https://localhost:9090/api/v1"
        Mockito.`when`(service.retrieveOriginLink(hash, provider)).thenReturn(originLink)

        // when
        val get = MockMvcRequestBuilders.get("/$hash")

        // then
        val perform = this.mockMvc.perform(get)

        // then
        perform.andExpect(MockMvcResultMatchers.status().isFound)
                .andExpect(redirectedUrl("https://localhost:9090/api/v1"))
                .andDo(MockMvcResultHandlers.print())
    }
}
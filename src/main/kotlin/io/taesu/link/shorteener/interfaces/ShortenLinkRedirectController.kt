package io.taesu.link.shorteener.interfaces

import io.taesu.link.shorteener.application.ShortenLinkRetrieveService
import io.taesu.link.app.config.LocalDateTimeProvider
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.view.RedirectView

/**
 * Created by itaesu on 2021/02/17.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Controller
class ShortenLinkRedirectController(
        val shortenLinkRetrieveService: ShortenLinkRetrieveService,
        val localDateTimeProvider: LocalDateTimeProvider
) {
    @GetMapping("{hash}")
    fun redirectHash(@PathVariable hash: String): RedirectView =
            RedirectView(shortenLinkRetrieveService.retrieveOriginLink(hash, localDateTimeProvider))
}
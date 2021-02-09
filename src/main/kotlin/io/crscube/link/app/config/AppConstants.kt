package io.crscube.link.app.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Created by itaesu on 2021/02/09.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
const val API_V1 = "/api/v1"
const val defaultTimeZone = "UTC"
const val defaultLanguage = "en"
const val defaultSuccessMessage = "Request was success"
const val defaultFailureMessage = "Request was fail"
const val initialSequence = 9999

@ConfigurationProperties("app.constants")
@ConstructorBinding
open class AppConstants(
        var domain: Domain
) {

    class Domain(val url: String)

    open val domainUrl: String
        get() = domain.url
}
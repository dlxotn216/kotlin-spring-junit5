package io.taesu.link.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

/**
 * Created by itaesu on 2021/03/01.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Configuration
class AppConfig {
    @Bean("LocalDateTimeProvider")
    fun localDateTimeProvider(): LocalDateTimeProvider = LocalDateTimeProvider()
}

open class LocalDateTimeProvider {
    operator fun invoke() = LocalDateTime.now()
}
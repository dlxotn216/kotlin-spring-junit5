package io.crscube.link

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import java.util.*
import javax.annotation.PostConstruct

@ConfigurationPropertiesScan
@SpringBootApplication
class CubelinkApplication {
    @PostConstruct
    fun onConstruct() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }
}

fun main(args: Array<String>) {
    runApplication<CubelinkApplication>(*args)
}

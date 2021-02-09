package io.crscube.link

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class CubelinkApplication

fun main(args: Array<String>) {
    runApplication<CubelinkApplication>(*args)
}

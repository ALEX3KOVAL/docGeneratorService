package ru.alex3koval.docGenerator.eventProducerApp

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import ru.alex3koval.transactionalOutBox.CdcProperties

@SpringBootApplication
@EnableConfigurationProperties(CdcProperties::class)
class EventProducerApp

fun main(args: Array<String>) {
    SpringApplicationBuilder(EventProducerApp::class.java)
        .web(WebApplicationType.NONE)
        .run(*args)
}

package ru.alex3koval.docGenerator.server.configuration

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.alex3koval.docGenerator.server.api.dto.request.CreateDocumentRequest
import ru.alex3koval.docGenerator.server.api.dto.request.deserialization.CreateDocumentRequestDeserializer
import ru.alex3koval.docGenerator.server.core.RegisterHttpBodySerializationModuleApplicationRunner

@Configuration
class HttpBodyDeserializerConfiguration {
    @Bean("httpBodyDeserializerModule")
    fun httpBodyDeserializers(): Module = SimpleModule().apply {
        addDeserializer(
            CreateDocumentRequest::class.java,
            CreateDocumentRequestDeserializer(
                templateIdParser = { node -> node.longValue().toULong() }
            )
        )
    }

    @Bean
    fun registerHttpBodySerializationModule(
        objectMapper: ObjectMapper,
        @Qualifier("httpBodyDeserializerModule") registerCustomDeserializerModule: Module
    ): RegisterHttpBodySerializationModuleApplicationRunner = RegisterHttpBodySerializationModuleApplicationRunner(
        objectMapper = objectMapper,
        registerCustomDeserializerModule = registerCustomDeserializerModule
    )
}

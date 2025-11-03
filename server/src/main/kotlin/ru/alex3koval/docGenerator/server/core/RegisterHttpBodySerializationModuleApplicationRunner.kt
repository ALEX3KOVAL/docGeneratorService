package ru.alex3koval.docGenerator.server.core

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner

class RegisterHttpBodySerializationModuleApplicationRunner(
    private val objectMapper: ObjectMapper,
    private val registerCustomDeserializerModule: Module
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        objectMapper.registerModule(registerCustomDeserializerModule)
    }
}

package ru.alex3koval.docGenerator.server.api

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.appImpl.command.factory.CreateDocumentCommandFactory
import ru.alex3koval.docGenerator.domain.command.CreateDocumentCommand
import ru.alex3koval.docGenerator.server.api.dto.request.CreateDocumentRequest

@RestController
@RequestMapping("document")
class DocumentController(
    private val createDocumentCommandFactory: CreateDocumentCommandFactory<*, *, Any>
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun add(
        @Validated @RequestBody body: CreateDocumentRequest<*>
    ): Mono<*> = createDocumentCommandFactory
        .create(
            dto = CreateDocumentCommand.DTO(
                templateId = body.templateId,
                rawModel = mapOf("hello" to "world"),
                format = body.format
            )
        )
        .execute()
}

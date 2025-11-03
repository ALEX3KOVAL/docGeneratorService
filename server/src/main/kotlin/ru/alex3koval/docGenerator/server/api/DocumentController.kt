package ru.alex3koval.docGenerator.server.api

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.alex3koval.docGenerator.appImpl.command.factory.CreateDocumentCommandFactory
import ru.alex3koval.docGenerator.appImpl.service.RetryService
import ru.alex3koval.docGenerator.domain.command.CreateDocumentCommand
import ru.alex3koval.docGenerator.domain.common.event.DocumentCreationHasBeenRequestedEvent
import ru.alex3koval.docGenerator.domain.common.vo.Topic
import ru.alex3koval.docGenerator.server.api.dto.request.CreateDocumentRequest
import ru.alex3koval.eventingContract.vo.EventStatus
import ru.alex3koval.eventingImpl.factory.TransactionalOutBoxReactiveEventPusherFactory

@RestController
@RequestMapping("document")
class DocumentController(
    private val createDocumentCommandFactory: CreateDocumentCommandFactory<*, *, Any>,
    @Qualifier("fileServiceRetry") private val retryService: RetryService,
    private val transactionalOutboxReactiveEventPusherFactory: TransactionalOutBoxReactiveEventPusherFactory<*>
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun add(
        @Validated @RequestBody body: CreateDocumentRequest<Any>
    ): Mono<*> {
        return retryService
            .withRetry(
                mono = createDocumentCommandFactory
                    .create(
                        dto = CreateDocumentCommand.DTO(
                            filename = body.filename,
                            templateId = body.templateId,
                            rawModel = body.model,
                            format = body.format
                        )
                    )
                    .execute(),
                onAllRetriesFailed = { exc ->
                    transactionalOutboxReactiveEventPusherFactory
                        .create()
                        .push(
                            Topic.DOCUMENT_GENERATION_DLT.value,
                            EventStatus.CREATED,
                            DocumentCreationHasBeenRequestedEvent(
                                filename = body.filename,
                                templateId = body.templateId,
                                format = body.format,
                                rawModel = body.model
                            )
                        )
                        .subscribeOn(Schedulers.boundedElastic())
                        .subscribe()
                }
            )
            .subscribeOn(Schedulers.boundedElastic())
    }
}

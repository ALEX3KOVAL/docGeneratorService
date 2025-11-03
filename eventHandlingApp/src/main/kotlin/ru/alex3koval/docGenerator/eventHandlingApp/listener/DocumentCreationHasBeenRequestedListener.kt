package ru.alex3koval.docGenerator.eventHandlingApp.listener

import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.appImpl.command.factory.CreateDocumentCommandFactory
import ru.alex3koval.docGenerator.domain.command.CreateDocumentCommand
import ru.alex3koval.docGenerator.domain.common.event.DocumentCreationHasBeenRequestedEvent
import ru.alex3koval.eventingContract.EventListener

class DocumentCreationHasBeenRequestedListener<TEMPLATE_ID : Any>(
    private val commandFactory: CreateDocumentCommandFactory<*, *, TEMPLATE_ID>
) : EventListener<DocumentCreationHasBeenRequestedEvent<*>, Mono<*>> {
    override fun onEvent(eventPayload: DocumentCreationHasBeenRequestedEvent<*>?): Mono<*>? = eventPayload
        ?.let {
            commandFactory
                .create(
                    dto = CreateDocumentCommand.DTO(
                        filename = eventPayload.filename,
                        templateId = eventPayload.templateId as TEMPLATE_ID,
                        rawModel = eventPayload.rawModel,
                        format = eventPayload.format
                    )
                )
        }
        ?.execute()
}

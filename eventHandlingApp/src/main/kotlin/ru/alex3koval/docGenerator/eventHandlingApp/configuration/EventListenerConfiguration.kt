package ru.alex3koval.docGenerator.eventHandlingApp.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.alex3koval.docGenerator.appImpl.command.factory.CreateDocumentCommandFactory
import ru.alex3koval.docGenerator.eventHandlingApp.listener.DocumentCreationHasBeenRequestedListener

@Configuration
class EventListenerConfiguration {
    @Bean
    fun documentCreationHasBeenRequestedListener(
        commandFactory: CreateDocumentCommandFactory<ULong, *, ULong>
    ): DocumentCreationHasBeenRequestedListener<ULong> =
        DocumentCreationHasBeenRequestedListener(commandFactory = commandFactory)
}

package ru.alex3koval.docGenerator.configuration.di

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.appImpl.command.factory.CreateDocumentCommandFactory
import ru.alex3koval.docGenerator.appImpl.service.DocumentServiceImpl
import ru.alex3koval.docGenerator.appImpl.service.FileServiceFacadeImpl
import ru.alex3koval.docGenerator.domain.common.repository.EventRepository
import ru.alex3koval.docGenerator.domain.repository.document.DocumentRepository
import ru.alex3koval.docGenerator.domain.repository.document.DocumentTemplateRepository
import ru.alex3koval.docGenerator.domain.service.DocumentService
import ru.alex3koval.docGenerator.domain.service.FileServiceFacade
import ru.alex3koval.docGenerator.domain.service.generator.DocumentGenerator
import ru.alex3koval.docGenerator.storage.repository.impl.TransactionalOutboxRepositoryImpl
import ru.alex3koval.docGenerator.storage.repository.impl.document.DocumentRepositoryImpl
import ru.alex3koval.docGenerator.storage.repository.impl.document.DocumentTemplateRepositoryImpl
import ru.alex3koval.docGenerator.storage.repository.orm.OrmDocumentRepository
import ru.alex3koval.docGenerator.storage.repository.orm.OrmDocumentTemplateRepository
import ru.alex3koval.docGenerator.storage.repository.orm.OrmEventRepository
import ru.alex3koval.eventingContract.dto.CreateEventWDTO
import ru.alex3koval.eventingImpl.factory.TransactionalOutBoxReactiveEventPusherFactory
import java.util.function.Function

@Configuration
class TypeDependentConfiguration(
    private val objectMapper: ObjectMapper,
    private val template: R2dbcEntityTemplate
) {
    @Bean
    fun documentRepository(
        ormDocumentRepository: OrmDocumentRepository<ULong, ULong, ULong>
    ): DocumentRepository<ULong, ULong, ULong> = DocumentRepositoryImpl(
        ormRepository = ormDocumentRepository,
        objectMapper = objectMapper,
        template = template
    )

    @Bean
    fun documentTemplateRepository(
        ormDocumentTemplateRepository: OrmDocumentTemplateRepository<ULong, ULong>,
    ): DocumentTemplateRepository<ULong, ULong> = DocumentTemplateRepositoryImpl(
        ormRepository = ormDocumentTemplateRepository,
        objectMapper = objectMapper,
        template = template
    )

    @Bean
    fun documentService(
        documentRepository: DocumentRepository<ULong, ULong, ULong>,
        documentTemplateRepository: DocumentTemplateRepository<ULong, ULong>
    ): DocumentService<ULong, ULong, ULong> = DocumentServiceImpl(
        documentRepository = documentRepository,
        documentTemplateRepository = documentTemplateRepository,
        objectMapper = objectMapper
    )

    @Bean
    fun createDocumentCommandFactory(
        documentService: DocumentService<ULong, ULong, ULong>,
        documentGenerator: DocumentGenerator,
        fileServiceFacade: FileServiceFacade<ULong>
    ): CreateDocumentCommandFactory<ULong, ULong, ULong> = CreateDocumentCommandFactory(
        documentService = documentService,
        documentGenerator = documentGenerator,
        fileServiceFacade = fileServiceFacade,
        objectMapper = objectMapper
    )

    @Bean
    fun fileServiceFacade(
        @Qualifier("fileServiceFacadeWebClient") fileServiceFacadeWebClient: WebClient
    ): FileServiceFacade<ULong> = FileServiceFacadeImpl(
        fileServiceFacadeWebClient,
        ULong::class.java
    )

    @Bean
    fun transactionalOutboxRepository(
        ormRepository: OrmEventRepository<ULong>
    ): EventRepository<ULong> = TransactionalOutboxRepositoryImpl(
        ormRepository = ormRepository,
        template = template
    )

    @Bean
    fun transactionOutboxReactiveEventPusherFactory(
        @Qualifier("reactiveTransactionalOutBoxPushingFunction") pushInDbFunction: Function<CreateEventWDTO, Mono<Long>>,
        objectMapper: ObjectMapper
    ): TransactionalOutBoxReactiveEventPusherFactory<Long> = TransactionalOutBoxReactiveEventPusherFactory(
        pushInDbFunction,
        objectMapper
    )

    @Bean("reactiveTransactionalOutBoxPushingFunction")
    fun pushInDbFunction(
        transactionalOutboxRepository: EventRepository<ULong>
    ): Function<CreateEventWDTO, Mono<Long>> {
        return Function { dto ->
            transactionalOutboxRepository::add
                .call(dto)
                .map { eventId -> eventId.toLong() }
        }
    }
}

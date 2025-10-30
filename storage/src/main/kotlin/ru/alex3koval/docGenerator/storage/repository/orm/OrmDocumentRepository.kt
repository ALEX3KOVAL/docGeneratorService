package ru.alex3koval.docGenerator.storage.repository.orm

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.storage.entity.Document

interface OrmDocumentRepository<ID : Any, FILE_ID, TEMPLATE_ID> :
    ReactiveCrudRepository<Document<ID, FILE_ID, TEMPLATE_ID>, ID> {
    @Query("INSERT INTO document (model, format, created_at, updated_at) VALUES (:#{document.model}, :#{document.format}, :#{document.createdAt}, :#{document.updatedAt}) RETURNING *")
    fun saveWithReturning(
        @Param("document") document: Document<ID, FILE_ID, TEMPLATE_ID>
    ): Mono<Document<ID, FILE_ID, TEMPLATE_ID>>
}

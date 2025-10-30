package ru.alex3koval.docGenerator.storage.repository.orm

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.storage.entity.DocumentTemplate

interface OrmDocumentTemplateRepository<ID : Any, FILE_ID> : ReactiveCrudRepository<DocumentTemplate<ID, FILE_ID>, ID> {
    @Query("INSERT INTO documentTemplate (model, format, file_id, clazz, created_at, updated_at) VALUES (:#{documentTemplate.model}, :#{documentTemplate.format}, :#{documentTemplate.fileId}, :#{documentTemplate.clazz}, :#{documentTemplate.createdAt}, :#{documentTemplate.updatedAt}) RETURNING *")
    fun saveWithReturning(
        @Param("documentTemplate") document: DocumentTemplate<ID, FILE_ID>
    ): Mono<DocumentTemplate<ID, FILE_ID>>
}

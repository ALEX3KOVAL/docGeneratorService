package ru.alex3koval.docGenerator.storage.repository.impl.document

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.Update
import org.springframework.data.relational.core.sql.SqlIdentifier
import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.repository.contract.dto.BaseCreateDocumentWDTO
import ru.alex3koval.docGenerator.domain.repository.contract.dto.BaseUpdatingDocumentWDTO
import ru.alex3koval.docGenerator.domain.repository.document.DocumentTemplateRepository
import ru.alex3koval.docGenerator.domain.repository.document.dto.DocumentTemplateRDTO
import ru.alex3koval.docGenerator.storage.entity.DocumentTemplate
import ru.alex3koval.docGenerator.storage.repository.orm.OrmDocumentTemplateRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

class DocumentTemplateRepositoryImpl<ID : Any, FILE_ID>(
    private val ormRepository: OrmDocumentTemplateRepository<ID, FILE_ID>,
    private val objectMapper: ObjectMapper,
    private val template: R2dbcEntityTemplate
) : DocumentTemplateRepository<ID, FILE_ID> {
    override fun get(id: ID): Mono<DocumentTemplateRDTO<ID, FILE_ID>> = ormRepository
        .findById(id)
        .mapNotNull { entity -> entity.toRdto() }

    override fun create(dto: BaseCreateDocumentWDTO<FILE_ID>): Mono<ID> {
        TODO("Not yet implemented")
    }

    override fun update(
        id: ID,
        dto: BaseUpdatingDocumentWDTO<FILE_ID>
    ): Mono<ID> {
        val fieldsForUpdating: MutableMap<SqlIdentifier?, Any?> = hashMapOf()

        if (dto.fileId != null) {
            fieldsForUpdating.put(SqlIdentifier.quoted("file_id"), dto.fileId)
        }

        if (dto.clazz != null) {
            fieldsForUpdating.put(SqlIdentifier.quoted("clazz"), dto.clazz)
        }

        if (dto.jsonModel != null) {
            fieldsForUpdating.put(SqlIdentifier.quoted("model"), dto.jsonModel)
        }

        if (fieldsForUpdating.isEmpty()) {
            return Mono.just<ID>(id)
        }

        fieldsForUpdating.put(
            SqlIdentifier.quoted("updated_at"),
            LocalDateTime.now(ZoneOffset.UTC)
        )

        val query = Query.query(
            Criteria.where("id").`is`(id)
        )

        return template
            .update(DocumentTemplate::class.java)
            .matching(query)
            .apply(Update.from(fieldsForUpdating))
            .thenReturn<ID>(id)
    }

    private fun DocumentTemplate<ID, FILE_ID>.toRdto(): DocumentTemplateRDTO<ID, FILE_ID> {
        val domainDto = objectMapper
            .readValue(jsonModel, object : TypeReference<Map<String, Any>>() {})
            .toDomainDto(clazz = clazz)

        return DocumentTemplateRDTO(
            id = id!!,
            fileId = fileId,
            domainDto = domainDto,
            clazz = clazz,
            format = format,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun Map<String, Any>.toDomainDto(clazz: String): BaseGeneratedDocDTO = objectMapper
        .convertValue(
            this,
            Class.forName(clazz) as Class<BaseGeneratedDocDTO>
        )
}

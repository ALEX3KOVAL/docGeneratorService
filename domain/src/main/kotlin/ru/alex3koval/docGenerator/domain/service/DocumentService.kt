package ru.alex3koval.docGenerator.domain.service

import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.entity.DocumentEntity
import ru.alex3koval.docGenerator.domain.entity.DocumentTemplateEntity
import ru.alex3koval.docGenerator.domain.repository.document.dto.DocumentRDTO
import ru.alex3koval.docGenerator.domain.repository.document.dto.DocumentTemplateRDTO
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat

abstract class DocumentService<ID, FILE_ID, TEMPLATE_ID : Any> {
    abstract fun createDocument(
        templateId: TEMPLATE_ID,
        fileId: FILE_ID,
        domainDto: BaseGeneratedDocDTO,
        format: DocumentFormat,
        clazz: String
    ): Mono<ID>

    abstract fun createDocumentTemplate(
        fileId: FILE_ID,
        domainDto: BaseGeneratedDocDTO,
        format: DocumentFormat,
        clazz: String
    ): Mono<TEMPLATE_ID>

    abstract fun get(id: ID): Mono<DocumentEntity<ID, FILE_ID>>

    abstract fun getTemplate(id: TEMPLATE_ID): Mono<DocumentTemplateEntity<TEMPLATE_ID, FILE_ID>>

    protected fun toEntity(
        rdto: DocumentRDTO<ID, FILE_ID, TEMPLATE_ID>
    ): DocumentEntity<ID, FILE_ID> = DocumentEntity(
        id = rdto.id,
        domainDto = rdto.domainDto,
        format = rdto.format,
        fileId = rdto.fileId
    )

    protected fun toEntity(
        rdto: DocumentTemplateRDTO<TEMPLATE_ID, FILE_ID>
    ): DocumentTemplateEntity<TEMPLATE_ID, FILE_ID> = DocumentTemplateEntity(
        id = rdto.id,
        fileId = rdto.fileId,
        domainDto = rdto.domainDto,
        format = rdto.format,
        clazz = rdto.clazz
    )
}

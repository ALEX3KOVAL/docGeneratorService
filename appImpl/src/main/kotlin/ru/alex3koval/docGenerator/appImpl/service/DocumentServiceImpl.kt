package ru.alex3koval.docGenerator.appImpl.service

import com.fasterxml.jackson.databind.ObjectMapper
import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.entity.DocumentEntity
import ru.alex3koval.docGenerator.domain.entity.DocumentTemplateEntity
import ru.alex3koval.docGenerator.domain.repository.document.DocumentRepository
import ru.alex3koval.docGenerator.domain.repository.document.DocumentTemplateRepository
import ru.alex3koval.docGenerator.domain.repository.document.dto.CreateDocumentTemplateWDTO
import ru.alex3koval.docGenerator.domain.repository.document.dto.CreateDocumentWDTO
import ru.alex3koval.docGenerator.domain.service.DocumentService
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat
import java.time.LocalDateTime
import java.time.ZoneOffset

class DocumentServiceImpl<ID : Any, FILE_ID, TEMPLATE_ID : Any>(
    private val documentRepository: DocumentRepository<ID, FILE_ID, TEMPLATE_ID>,
    private val documentTemplateRepository: DocumentTemplateRepository<TEMPLATE_ID, FILE_ID>,
    private val objectMapper: ObjectMapper
) : DocumentService<ID, FILE_ID, TEMPLATE_ID>() {
    override fun get(id: ID): Mono<DocumentEntity<ID, FILE_ID>> = documentRepository
        .get(id)
        .mapNotNull { rdto -> toEntity(rdto) }

    override fun getTemplate(id: TEMPLATE_ID): Mono<DocumentTemplateEntity<TEMPLATE_ID, FILE_ID>> = documentTemplateRepository
        .get(id)
        .mapNotNull { rdto -> toEntity(rdto) }

    override fun createDocument(
        templateId: TEMPLATE_ID,
        fileId: FILE_ID,
        domainDto: BaseGeneratedDocDTO,
        format: DocumentFormat,
        clazz: String
    ): Mono<ID> = documentRepository.create(
        toCreationWdto(
            templateId = templateId,
            fileId = fileId,
            domainDto = domainDto,
            format = format,
            clazz = clazz
        )
    )

    override fun createDocumentTemplate(
        fileId: FILE_ID,
        domainDto: BaseGeneratedDocDTO,
        format: DocumentFormat,
        clazz: String
    ): Mono<TEMPLATE_ID> = documentTemplateRepository.create(
        toCreationWdto(
            fileId = fileId,
            domainDto = domainDto,
            format = format,
            clazz = clazz
        )
    )

    private fun toCreationWdto(
        templateId: TEMPLATE_ID,
        fileId: FILE_ID,
        clazz: String,
        domainDto: BaseGeneratedDocDTO,
        format: DocumentFormat
    ): CreateDocumentWDTO<FILE_ID, TEMPLATE_ID> = run {
        val now = LocalDateTime.now(ZoneOffset.UTC)

        CreateDocumentWDTO(
            templateId = templateId,
            jsonModel = objectMapper.writeValueAsString(domainDto),
            format = format,
            clazz = clazz,
            fileId = fileId,
            createdAt = now,
            updatedAt = now
        )
    }

    private fun toCreationWdto(
        fileId: FILE_ID,
        clazz: String,
        domainDto: BaseGeneratedDocDTO,
        format: DocumentFormat
    ): CreateDocumentTemplateWDTO<FILE_ID> = run {
        val now = LocalDateTime.now(ZoneOffset.UTC)

        CreateDocumentTemplateWDTO(
            jsonModel = objectMapper.writeValueAsString(domainDto),
            format = format,
            clazz = clazz,
            fileId = fileId,
            createdAt = now,
            updatedAt = now
        )
    }
}

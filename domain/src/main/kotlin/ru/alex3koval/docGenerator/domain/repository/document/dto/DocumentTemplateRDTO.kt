package ru.alex3koval.docGenerator.domain.repository.document.dto

import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.repository.contract.dto.BaseDocumentRDTO
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat
import java.time.LocalDateTime

class DocumentTemplateRDTO<ID: Any, FILE_ID>(
    override val id: ID,
    override val clazz: String,
    val domainDto: BaseGeneratedDocDTO,
    fileId: FILE_ID,
    format: DocumentFormat,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime
) : BaseDocumentRDTO<ID, FILE_ID, ID>(
    fileId = fileId,
    format = format,
    createdAt = createdAt,
    updatedAt = updatedAt
)

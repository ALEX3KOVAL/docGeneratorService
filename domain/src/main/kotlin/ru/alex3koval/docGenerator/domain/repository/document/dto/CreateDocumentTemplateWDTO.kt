package ru.alex3koval.docGenerator.domain.repository.document.dto

import ru.alex3koval.docGenerator.domain.repository.contract.dto.BaseCreateDocumentWDTO
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat
import java.time.LocalDateTime

class CreateDocumentTemplateWDTO<FILE_ID>(
    override val jsonModel: String,
    override val fileId: FILE_ID,
    override val clazz: String,
    override val format: DocumentFormat,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime
) : BaseCreateDocumentWDTO<FILE_ID>()

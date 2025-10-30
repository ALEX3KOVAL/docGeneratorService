package ru.alex3koval.docGenerator.domain.repository.contract.dto

import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat
import java.time.LocalDateTime

abstract class BaseDocumentRDTO<ID, FILE_ID, TEMPLATE_ID>(
    override val fileId: FILE_ID,
    override val format: DocumentFormat,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime
) : BaseDocumentDTO<FILE_ID>() {
    abstract val id: ID

}

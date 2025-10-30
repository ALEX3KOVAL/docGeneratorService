package ru.alex3koval.docGenerator.domain.repository.contract.dto

import ru.alex3koval.docGenerator.domain.vo.DocumentFormat

abstract class BaseDocumentDTO<FILE_ID> : TimestampDTO {
    abstract val fileId: FILE_ID
    abstract val format: DocumentFormat
    abstract val clazz: String
}

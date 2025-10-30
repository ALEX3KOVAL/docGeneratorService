package ru.alex3koval.docGenerator.domain.repository.contract.dto

import ru.alex3koval.docGenerator.domain.vo.DocumentFormat

abstract class BaseUpdatingDocumentWDTO<FILE_ID> {
    abstract val fileId: FILE_ID?
    abstract val jsonModel: String?
    abstract val clazz: String?
    abstract val format: DocumentFormat?
}

package ru.alex3koval.docGenerator.domain.repository.contract.dto

abstract class BaseCreateDocumentWDTO<FILE_ID> : BaseDocumentDTO<FILE_ID>() {
    abstract val jsonModel: String
}

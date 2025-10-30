package ru.alex3koval.docGenerator.domain.repository.document.dto

import ru.alex3koval.docGenerator.domain.repository.contract.dto.BaseUpdatingDocumentWDTO
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat

data class UpdateDocumentWDTO<FILE_ID, TEMPLATE_ID>(
    val templateId: TEMPLATE_ID? = null,
    override val fileId: FILE_ID? = null,
    override val jsonModel: String? = null,
    override val clazz: String? = null,
    override val format: DocumentFormat? = null
) : BaseUpdatingDocumentWDTO<FILE_ID>()

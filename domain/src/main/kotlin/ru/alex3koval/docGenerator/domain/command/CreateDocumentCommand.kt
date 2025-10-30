package ru.alex3koval.docGenerator.domain.command

import ru.alex3koval.docGenerator.domain.contract.Command
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat

interface CreateDocumentCommand<T> : Command<T> {
    data class DTO<TEMPLATE_ID>(
        val templateId: TEMPLATE_ID,
        val rawModel: Map<String, Any>,
        val format: DocumentFormat
    )
}

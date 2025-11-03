package ru.alex3koval.docGenerator.domain.common.event

import ru.alex3koval.docGenerator.domain.vo.DocumentFormat

data class DocumentCreationHasBeenRequestedEvent<TEMPLATE_ID : Any>(
    val filename: String,
    val templateId: TEMPLATE_ID,
    val format: DocumentFormat,
    val rawModel: Map<String, Any>
)

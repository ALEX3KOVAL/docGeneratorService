package ru.alex3koval.docGenerator.domain.service.generator

import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat
import java.io.InputStream

interface DocumentGenerator {
    fun <DTO : BaseGeneratedDocDTO> generate(
        dto: DTO,
        template: InputStream,
        format: DocumentFormat
    ): Mono<InputStream>
}

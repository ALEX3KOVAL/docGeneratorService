package ru.alex3koval.docGenerator.appImpl.service.generator

import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.service.FileGenerator
import ru.alex3koval.docGenerator.domain.service.generator.DocumentGenerator
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat
import java.io.InputStream

class DocumentGeneratorImpl(
    private val fileGenerator: FileGenerator
) : DocumentGenerator {
    override fun <DTO : BaseGeneratedDocDTO> generate(
        dto: DTO,
        template: InputStream,
        format: DocumentFormat
    ): Mono<InputStream> = when (format) {
        DocumentFormat.DOCX -> fileGenerator
            .generateDocx(
                template = template,
                dto = dto
            )

        DocumentFormat.PDF -> fileGenerator.generatePdf(
            template = template,
            dto = dto
        )

        else -> TODO("Not yet implemented for another document formats")
    }
}
package ru.alex3koval.docGenerator.appImpl.service.factory

import ru.alex3koval.docGenerator.appImpl.service.generator.DocxGeneratorImpl
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.service.generator.DocxGenerator
import java.io.InputStream

class DocxGeneratorFactory {
    fun <DTO: BaseGeneratedDocDTO> create(template: InputStream, dto: DTO): DocxGenerator = DocxGeneratorImpl(
        template = template,
        dto = dto
    )
}

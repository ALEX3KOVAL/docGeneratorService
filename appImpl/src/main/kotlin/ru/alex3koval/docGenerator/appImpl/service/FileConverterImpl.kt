package ru.alex3koval.docGenerator.appImpl.service

import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.service.FileConverter
import ru.alex3koval.docGenerator.domain.service.UnoconvFacade
import java.io.InputStream

class FileConverterImpl(
    private val unoconvFacade: UnoconvFacade
) : FileConverter {
    override fun toPdf(docx: InputStream): Mono<InputStream> = unoconvFacade.convertToPdf(docx = docx)
}
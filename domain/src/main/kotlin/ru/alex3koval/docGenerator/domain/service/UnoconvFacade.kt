package ru.alex3koval.docGenerator.domain.service

import reactor.core.publisher.Mono
import java.io.InputStream

interface UnoconvFacade {
    fun convertToPdf(docx: InputStream): Mono<InputStream>
}

package ru.alex3koval.docGenerator.domain.service

import reactor.core.publisher.Mono
import java.io.InputStream

interface FileConverter {
    fun toPdf(docx: InputStream): Mono<InputStream>
}

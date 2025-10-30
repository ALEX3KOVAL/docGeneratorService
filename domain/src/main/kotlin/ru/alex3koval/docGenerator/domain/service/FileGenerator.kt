package ru.alex3koval.docGenerator.domain.service

import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import java.io.InputStream
import java.nio.file.Path

interface FileGenerator<T> {
    fun generateDocx(template: InputStream, data: BaseGeneratedDocDTO): Mono<Path>
    fun generatePdf(template: InputStream, data: BaseGeneratedDocDTO): Mono<Path>
}

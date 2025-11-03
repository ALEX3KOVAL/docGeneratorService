package ru.alex3koval.docGenerator.domain.service

import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import java.io.InputStream

interface FileGenerator {
    fun generateDocx(template: InputStream, dto: BaseGeneratedDocDTO): Mono<InputStream>
    fun generatePdf(template: InputStream, dto: BaseGeneratedDocDTO): Mono<InputStream>
}

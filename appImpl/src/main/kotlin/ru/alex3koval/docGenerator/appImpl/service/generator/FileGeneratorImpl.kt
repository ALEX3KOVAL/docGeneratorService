package ru.alex3koval.docGenerator.appImpl.service.generator

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.alex3koval.docGenerator.appImpl.service.factory.DocxGeneratorFactory
import ru.alex3koval.docGenerator.domain.common.core.Modifier
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.contract.DocumentContent
import ru.alex3koval.docGenerator.domain.contract.DocumentModifier
import ru.alex3koval.docGenerator.domain.service.FileConverter
import ru.alex3koval.docGenerator.domain.service.FileGenerator
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.findAnnotations

class FileGeneratorImpl(
    private val docxGeneratorFactory: DocxGeneratorFactory,
    private val fileConverter: FileConverter
) : FileGenerator {
    override fun generateDocx(
        template: InputStream,
        dto: BaseGeneratedDocDTO
    ): Mono<InputStream> = with(
        docxGeneratorFactory.create(template = template, dto = dto)
    ) {
        val modifier = dto.findModifier()

        modifier?.modify(dto = dto, generator = this)
        generate()
    }
        .map { xwpfDocument ->
            val byteArray = xwpfDocument.use { doc ->
                ByteArrayOutputStream().use { out ->
                    doc.write(out)
                    out.toByteArray()
                }
            }

            ByteArrayInputStream(byteArray) as InputStream
        }
        .publishOn(Schedulers.boundedElastic())

    override fun generatePdf(
        template: InputStream,
        dto: BaseGeneratedDocDTO
    ): Mono<InputStream> = generateDocx(template = template, dto = dto)
        .publishOn(Schedulers.boundedElastic())
        .flatMap { generatedDocx -> fileConverter.toPdf(docx = generatedDocx) }

    private fun <DTO: BaseGeneratedDocDTO> DTO.findModifier(): DocumentModifier<DTO>? = this::class
        .findAnnotation<Modifier>()
        ?.clazz
        ?.createInstance()
        ?.let { it as DocumentModifier<DTO>? }
}

package ru.alex3koval.docGenerator.appImpl.service.generator

import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFRun
import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.service.generator.DocxGenerator
import ru.alex3koval.docGenerator.domain.service.generator.DocxListGenerator
import ru.alex3koval.docGenerator.domain.service.generator.DocxTableGenerator
import java.io.InputStream
import kotlin.getValue
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType

class DocxGeneratorImpl<DTO: BaseGeneratedDocDTO>(
    template: InputStream,
    private val dto: DTO
) : DocxGenerator() {
    private val fields = mutableMapOf<String, String>()
    private val tables = mutableListOf<DocxTableGenerator>()
    override val document: XWPFDocument = XWPFDocument(template)
    private val lists = mutableListOf<DocxListGenerator<*>>()

    override fun generate(): Mono<XWPFDocument> = Mono.fromCallable {
        bind()

        document.scan {
            it.setText(
                it.text().putData(),
                0
            )
        }

        tables.forEach { it.render() }
        lists.forEach { it.render() }

        document.apply { validate().getOrThrow() }
    }

    override fun <T> list(
        listName: String,
        headerBuilder: (XWPFRun.() -> Unit)?,
        items: List<T>,
        lineBreakChar: Char,
        neededDotInEnd: Boolean,
        builder: DocxListGenerator<T>.() -> Unit
    ) {
        lists += DocxListGeneratorImpl(
            name = listName,
            headerBuilder = headerBuilder,
            items = items,
            lineBreakChar = lineBreakChar,
            neededDotInEnd = neededDotInEnd,
            document = document
        ).apply { builder() }
    }

    private fun bind() {
        dto::class.members.filterIsInstance<KProperty1<*, *>>().forEach {
            when (it.returnType) {
                String::class.createType() -> {
                    val p = it as KProperty1<DTO, String>
                    fields[it.name] = it.getValue(dto, p)
                }
            }
        }
    }

    private fun XWPFDocument.scan(processor: (XWPFRun) -> Unit) {
        tables.forEach { t ->
            t.rows.forEach { r ->
                r.tableCells.forEach { c ->
                    c.paragraphs.forEach { pg ->
                        pg.runs.forEach { pgr -> processor(pgr) }
                    }
                }
            }
        }

        footerList.forEach { footer ->
            footer.paragraphs.forEach { p ->
                p.runs.forEach { pr -> processor(pr) }
            }
        }

        paragraphs.forEach { p ->
            p.runs.forEach { r -> processor(r) }
        }
    }

    private fun XWPFDocument.validate(): Result<Unit> = runCatching {
        val pattern = "[$][{][A-Za-z0-9]+[}]".toRegex()
        val extractor = XWPFWordExtractor(this)
        val matches = pattern.findAll(extractor.text)

        val brokenList = matches.fold("") { acc, matchResult ->
            if (acc.isNotEmpty()) "$acc, ${matchResult.value}" else matchResult.value
        }

        if (brokenList.isNotEmpty()) {
            throw RuntimeException("После генерации документа остались незаполненные якори: $brokenList")
        }
    }

    private fun String.putData(): String = fields
        .entries
        .fold(initial = this) { acc, (key, value) ->
            acc.replace("\${$key}", value)
        }
}

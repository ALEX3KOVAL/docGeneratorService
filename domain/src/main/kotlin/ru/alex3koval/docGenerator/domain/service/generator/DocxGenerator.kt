package ru.alex3koval.docGenerator.domain.service.generator

import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFRun
import reactor.core.publisher.Mono

abstract class DocxGenerator {
    abstract val document: XWPFDocument

    abstract fun generate(): Mono<XWPFDocument>
    abstract fun <T> list(
        listName: String,
        headerBuilder: (XWPFRun.() -> Unit)? = null,
        items: List<T>,
        lineBreakChar: Char = ';',
        neededDotInEnd: Boolean = false,
        builder: DocxListGenerator<T>.() -> Unit
    )
}

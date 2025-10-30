package ru.alex3koval.docGenerator.domain.contract

import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFRun

abstract class DocxGenerator<T : BaseGeneratedDocDTO> {
    abstract val document: XWPFDocument

    abstract fun generate(): Result<XWPFDocument>
    abstract fun <T> list(
        listName: String,
        headerBuilder: (XWPFRun.() -> Unit)? = null,
        items: List<T>,
        lineBreakChar: Char = ';',
        neededDotInEnd: Boolean = false,
        builder: DocxListGenerator<T>.() -> Unit
    )
}

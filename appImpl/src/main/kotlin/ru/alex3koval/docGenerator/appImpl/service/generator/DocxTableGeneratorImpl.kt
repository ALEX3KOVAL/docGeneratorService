package ru.alex3koval.docGenerator.appImpl.service.generator

import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFTable
import org.apache.poi.xwpf.usermodel.XWPFTableRow
import ru.alex3koval.docGenerator.domain.service.generator.DocxTableGenerator
import ru.alex3koval.docGenerator.domain.service.generator.DocxTableRowGenerator

class DocxTableGeneratorImpl<T>(
    private val document: XWPFDocument,
    private val tableName: String,
    private val data: List<T>
) : DocxTableGenerator {
    private val rowRenderer: DocxTableRowGenerator.(T) -> Unit = {}

    override fun render() {
        val table = document.tables.fold(null) { acc: XWPFTable?, t: XWPFTable ->
            if (t.getRow(0).getCell(0).text.trim().contains("\${$tableName}")) t else acc
        } ?: throw RuntimeException("Table [$tableName] not found in template")

        table.getRow(0).getCell(0).removeParagraph(0)
        var row: XWPFTableRow? = null

        data.forEach {
            row = if (row == null) table.getRow(0) else table.createRow()
            val rowContext = DocxTableRowGeneratorImpl(row = row!!)
            rowContext.run {
                rowRenderer(it)
            }
        }
    }
}

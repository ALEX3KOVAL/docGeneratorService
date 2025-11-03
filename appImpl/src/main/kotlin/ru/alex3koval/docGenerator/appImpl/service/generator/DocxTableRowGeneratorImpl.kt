package ru.alex3koval.docGenerator.appImpl.service.generator

import org.apache.poi.xwpf.usermodel.XWPFTableCell
import org.apache.poi.xwpf.usermodel.XWPFTableRow
import ru.alex3koval.docGenerator.domain.service.generator.DocxTableRowGenerator

class DocxTableRowGeneratorImpl(
    private val row: XWPFTableRow
) : DocxTableRowGenerator {
    private var cellCount = -1

    override fun cell(text: String): XWPFTableCell {
        cellCount++

        val cell = if (cellCount < row.tableCells.size) {
            row.getCell(cellCount)
        } else {
            row.createCell()
        }

        return cell.apply { this.text = text }
    }
}

package ru.alex3koval.docGenerator.domain.service.generator

import org.apache.poi.xwpf.usermodel.XWPFTableCell

interface DocxTableRowGenerator {
    fun cell(text: String): XWPFTableCell
}

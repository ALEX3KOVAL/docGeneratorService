package ru.alex3koval.docGenerator.appImpl.service.generator

import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import org.apache.poi.xwpf.usermodel.XWPFRun
import org.apache.xmlbeans.XmlCursor
import ru.alex3koval.docGenerator.domain.service.generator.DocxListGenerator

class DocxListGeneratorImpl<T>(
    private val name: String,
    private val headerBuilder: (XWPFRun.() -> Unit)?,
    private val items: List<T>,
    private val lineBreakChar: Char,
    private val neededDotInEnd: Boolean,
    private val document: XWPFDocument
) : DocxListGenerator<T> {
    private lateinit var currentCursor: XmlCursor

    var itemRenderer: DocxListGeneratorImpl<T>.(T, XWPFParagraph, String) -> Unit = { _, _, _ -> }

    override fun item(render: DocxListGenerator<T>.(T, XWPFParagraph, String) -> Unit) {
        itemRenderer = render
    }

    override fun render() {
        val startParagraph = document.paragraphs.fold(null) { acc: XWPFParagraph?, p: XWPFParagraph ->
            if (p.text.contains("\${$name}")) p else acc
        } ?: throw RuntimeException("Paragraph [$name] not found in template")

        startParagraph.runs.forEach {
            it.setText("", 0)
        }

        currentCursor = startParagraph.ctp.newCursor().apply { toNextSibling() }

        var currentP: XWPFParagraph? = null

        headerBuilder?.let { headerClosure ->
            startParagraph.createRun().apply {
                headerClosure()
                addBreak()
            }

            currentCursor = startParagraph.ctp.newCursor()
            currentCursor.toNextSibling()
        }

        items
            .slice(0..<items.lastIndex)
            .forEach { item ->
                currentP = if (currentP == null) startParagraph else paragraph { }
                itemRenderer(item, currentP!!, "$lineBreakChar")
            }

        items
            .lastOrNull()
            ?.let { item ->
                currentP = if (currentP == null) startParagraph else paragraph { }
                itemRenderer(item, currentP!!, if (neededDotInEnd) "." else "")
            }
    }

    override fun paragraph(builder: XWPFParagraph.() -> Unit): XWPFParagraph {
        val p = document.insertNewParagraph(currentCursor)
        p.builder()
        currentCursor = p.ctp.newCursor()
        currentCursor.toNextSibling()

        return p
    }
}

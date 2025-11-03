package ru.alex3koval.docGenerator.server.api.dto.request.deserialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.ObjectCodec
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat
import ru.alex3koval.docGenerator.server.api.dto.request.CreateDocumentRequest
import java.lang.RuntimeException
import java.util.function.Function

class CreateDocumentRequestDeserializer<T : Any>(
    private val templateIdParser: Function<JsonNode, T>,
) : StdDeserializer<CreateDocumentRequest<T>>(CreateDocumentRequest::class.java) {
    override fun deserialize(
        p: JsonParser?,
        ctxt: DeserializationContext?
    ): CreateDocumentRequest<T>? {
        val oc: ObjectCodec = p!!.codec
        val node: JsonNode = oc.readTree(p)
        val modelNode: JsonNode? = node.get("model")
        var modelProps: Map<String, Any>? = null

        if (modelNode?.isObject == true) {
            modelProps = modelNode.convertJsonNodeToMap(codec = oc)
        }

        return CreateDocumentRequest(
            filename = node.get("filename").asText(),
            templateId = node
                .get("templateId")
                ?.run { templateIdParser.apply(this) }
                ?: throw RuntimeException("Не найден узел с именем templateId"),
            format = ctxt!!.readValue(
                node.get("format").traverse(oc),
                DocumentFormat::class.java
            ),
            model = modelProps ?: throw RuntimeException(
                "JsonNode с свойствами модели равен null: $modelNode\n" +
                "или не является объектом: ${modelNode?.isObject}"
            )
        )
    }

    private fun JsonNode.convertJsonValue(codec: ObjectCodec): Any? {
        return when {
            isObject -> convertJsonNodeToMap(codec)
            isArray -> convertJsonArray(codec)
            isTextual -> asText()
            isNumber -> convertNumber(this)
            isBoolean -> booleanValue()
            isNull -> null
            else -> asText()
        }
    }

    private fun JsonNode.convertJsonArray(codec: ObjectCodec): List<Any?> = map { element ->
        element.convertJsonValue(codec)
    }

    private fun convertNumber(node: JsonNode): Number {
        return when {
            node.isInt -> node.intValue()
            node.isLong -> node.longValue()
            node.isDouble -> node.doubleValue()
            node.isFloat -> node.floatValue()
            node.isBigInteger -> node.bigIntegerValue()
            else -> node.asLong()
        }
    }

    private fun JsonNode.convertJsonNodeToMap(codec: ObjectCodec): Map<String, Any> {
        val result = mutableMapOf<String, Any>()

        properties()
        .forEach { (fieldName, node) ->
            result[fieldName] = node.convertJsonValue(codec = codec)
                ?: throw RuntimeException("Не удалось конвертировать JsonNode в Map<String, Any>")
        }

        return result
    }
}

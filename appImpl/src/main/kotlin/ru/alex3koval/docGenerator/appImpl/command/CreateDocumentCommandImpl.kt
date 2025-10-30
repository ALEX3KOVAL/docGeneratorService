package ru.alex3koval.docGenerator.appImpl.command

import com.fasterxml.jackson.databind.ObjectMapper
import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.command.CreateDocumentCommand
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.contract.DocumentGenerator
import ru.alex3koval.docGenerator.domain.service.DocumentService
import ru.alex3koval.docGenerator.domain.service.FileServiceFacade
import ru.alex3koval.docGenerator.domain.service.dto.UploadFileRequestDTO
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat

class CreateDocumentCommandImpl<DOC_ID, FILE_ID, TEMPLATE_ID : Any>(
    private val templateId: TEMPLATE_ID,
    private val format: DocumentFormat,
    private val rawModel: Map<String, Any>,
    private val documentService: DocumentService<DOC_ID, FILE_ID, TEMPLATE_ID>,
    private val documentGenerator: DocumentGenerator,
    private val fileServiceFacade: FileServiceFacade<FILE_ID>,
    private val objectMapper: ObjectMapper
) : CreateDocumentCommand<DOC_ID> {
    override fun execute(): Mono<DOC_ID> = documentService
        .getTemplate(templateId)
        .flatMap { entity ->
            Mono
                .just(rawModel.mapToDomainDto(entity.clazz))
                .withContextWrite(entity.clazz)
        }
        .zipWhen { domainDto -> documentGenerator.generate(domainDto) }
        .flatMap { tuple ->
            fileServiceFacade
                .uploadFile(
                    dto = with(tuple.t2) {
                        UploadFileRequestDTO(
                            filename = name,
                            hash = hash,
                            size = size,
                            format = format,
                            stream = stream
                        )
                    }
                )
                .zipWhen { fileId -> Mono.just(tuple.t1) }
        }
        .flatMap { tuple ->
            Mono.deferContextual { ctxView ->
                documentService.createDocument(
                    templateId = templateId,
                    fileId = tuple.t1,
                    domainDto = tuple.t2,
                    format = format,
                    clazz = ctxView.get("clazz")
                )
            }
        }

    private fun <T> Mono<T>.withContextWrite(clazz: String): Mono<T> = this
        .contextWrite { ctx -> ctx.put("clazz", clazz) }

    private fun Map<String, Any>.mapToDomainDto(clazz: String): BaseGeneratedDocDTO = objectMapper
        .convertValue(
            this,
            Class.forName(clazz) as Class<BaseGeneratedDocDTO>
        )
}

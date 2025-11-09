package ru.alex3koval.docGenerator.appImpl.command

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.alex3koval.docGenerator.domain.command.CreateDocumentCommand
import ru.alex3koval.docGenerator.domain.contract.BaseGeneratedDocDTO
import ru.alex3koval.docGenerator.domain.service.DocumentService
import ru.alex3koval.docGenerator.domain.service.FileServiceFacade
import ru.alex3koval.docGenerator.domain.service.dto.UploadFileRequestDTO
import ru.alex3koval.docGenerator.domain.service.generator.DocumentGenerator
import ru.alex3koval.docGenerator.domain.vo.DocumentFormat

open class CreateDocumentCommandImpl<DOC_ID, FILE_ID, TEMPLATE_ID : Any>(
    private val filename: String,
    private val templateId: TEMPLATE_ID,
    private val format: DocumentFormat,
    private val rawModel: Map<String, Any>,
    private val documentService: DocumentService<DOC_ID, FILE_ID, TEMPLATE_ID>,
    private val documentGenerator: DocumentGenerator,
    private val fileServiceFacade: FileServiceFacade<FILE_ID>,
    private val objectMapper: ObjectMapper
) : CreateDocumentCommand<DOC_ID> {
    @Transactional
    override fun execute(): Mono<DOC_ID> = documentService
        .getTemplate(templateId)
        .zipWhen { entity -> fileServiceFacade.getFile(id = entity.fileId) }
        .flatMap { tuple ->
            Mono
                .just(rawModel.mapToDomainDto(tuple.t1.clazz))
                .zipWhen { Mono.just(tuple.t2) }
                .withTemplateClazzContextWrite(tuple.t1.clazz)
        }
        .publishOn(Schedulers.parallel())
        .flatMap { tuple ->
            documentGenerator
                .generate(
                    dto = tuple.t1,
                    template = tuple.t2,
                    format = format
                )
                .zipWhen { Mono.just(tuple.t1) }
        }
        .flatMap { tuple ->
            fileServiceFacade
                .uploadFile(
                    dto = with(tuple.t1) {
                        UploadFileRequestDTO(
                            filename = filename,
                            format = format,
                            stream = tuple.t1
                        )
                    }
                )
                .zipWhen { fileId -> Mono.just(tuple.t2) }
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

    private fun Map<String, Any>.mapToDomainDto(clazz: String): BaseGeneratedDocDTO = objectMapper
        .convertValue(
            this,
            Class.forName(clazz) as Class<BaseGeneratedDocDTO>
        )

    private fun <T> Mono<T>.withTemplateClazzContextWrite(clazz: String): Mono<T> = this
        .contextWrite { ctx -> ctx.put("clazz", clazz) }
}

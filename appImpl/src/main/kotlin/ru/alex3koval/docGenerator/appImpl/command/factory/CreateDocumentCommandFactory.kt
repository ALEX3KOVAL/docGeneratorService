package ru.alex3koval.docGenerator.appImpl.command.factory

import com.fasterxml.jackson.databind.ObjectMapper
import ru.alex3koval.docGenerator.appImpl.command.CreateDocumentCommandImpl
import ru.alex3koval.docGenerator.domain.command.CreateDocumentCommand
import ru.alex3koval.docGenerator.domain.command.factory.CommandFactory
import ru.alex3koval.docGenerator.domain.contract.DocumentGenerator
import ru.alex3koval.docGenerator.domain.service.DocumentService
import ru.alex3koval.docGenerator.domain.service.FileServiceFacade

class CreateDocumentCommandFactory<ID, FILE_ID, TEMPLATE_ID : Any>(
    private val documentService: DocumentService<ID, FILE_ID, TEMPLATE_ID>,
    private val documentGenerator: DocumentGenerator,
    private val fileServiceFacade: FileServiceFacade<FILE_ID>,
    private val objectMapper: ObjectMapper
) : CommandFactory<CreateDocumentCommand<ID>, CreateDocumentCommand.DTO<TEMPLATE_ID>> {
    override fun create(
        dto: CreateDocumentCommand.DTO<TEMPLATE_ID>
    ): CreateDocumentCommand<ID> = CreateDocumentCommandImpl(
        templateId = dto.templateId,
        format = dto.format,
        rawModel = dto.rawModel,
        documentService = documentService,
        documentGenerator = documentGenerator,
        fileServiceFacade = fileServiceFacade,
        objectMapper = objectMapper
    )
}

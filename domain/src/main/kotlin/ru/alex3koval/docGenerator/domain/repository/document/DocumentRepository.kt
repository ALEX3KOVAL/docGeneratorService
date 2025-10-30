package ru.alex3koval.docGenerator.domain.repository.document

import ru.alex3koval.docGenerator.domain.repository.contract.repository.BaseRepository
import ru.alex3koval.docGenerator.domain.repository.document.dto.CreateDocumentWDTO
import ru.alex3koval.docGenerator.domain.repository.document.dto.DocumentRDTO
import ru.alex3koval.docGenerator.domain.repository.document.dto.UpdateDocumentWDTO

interface DocumentRepository<ID : Any, FILE_ID, TEMPLATE_ID> :
    BaseRepository<ID, CreateDocumentWDTO<FILE_ID, TEMPLATE_ID>, UpdateDocumentWDTO<FILE_ID, TEMPLATE_ID>, DocumentRDTO<ID, FILE_ID, TEMPLATE_ID>>

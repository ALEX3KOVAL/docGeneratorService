package ru.alex3koval.docGenerator.domain.repository.document

import ru.alex3koval.docGenerator.domain.repository.contract.dto.BaseCreateDocumentWDTO
import ru.alex3koval.docGenerator.domain.repository.contract.dto.BaseUpdatingDocumentWDTO
import ru.alex3koval.docGenerator.domain.repository.contract.repository.BaseRepository
import ru.alex3koval.docGenerator.domain.repository.document.dto.DocumentTemplateRDTO

interface DocumentTemplateRepository<ID : Any, FILE_ID> :
    BaseRepository<ID, BaseCreateDocumentWDTO<FILE_ID>, BaseUpdatingDocumentWDTO<FILE_ID>, DocumentTemplateRDTO<ID, FILE_ID>>

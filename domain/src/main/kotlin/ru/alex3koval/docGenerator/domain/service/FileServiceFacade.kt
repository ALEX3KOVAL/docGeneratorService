package ru.alex3koval.docGenerator.domain.service

import reactor.core.publisher.Mono
import ru.alex3koval.docGenerator.domain.service.dto.UploadFileRequestDTO
import java.io.InputStream

interface FileServiceFacade<FILE_ID> {
    fun uploadFile(dto: UploadFileRequestDTO): Mono<FILE_ID>
    fun getFile(id: FILE_ID): Mono<InputStream>
}

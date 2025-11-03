package ru.alex3koval.docGenerator.appImpl.service

import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.alex3koval.docGenerator.appImpl.core.extensions.buildByteArrayInputStream
import ru.alex3koval.docGenerator.domain.service.FileServiceFacade
import ru.alex3koval.docGenerator.domain.service.dto.UploadFileRequestDTO
import java.io.InputStream

class FileServiceFacadeImpl<ID : Any>(
    private val webClient: WebClient,
    private val fileIdClazz: Class<ID>
) : FileServiceFacade<ID> {
    override fun uploadFile(dto: UploadFileRequestDTO): Mono<ID> = webClient
        .post()
        .contentType(MediaType.APPLICATION_PDF)
        .header("X-Filename", dto.filename)
        .body(
            BodyInserters.fromDataBuffers(
                DataBufferUtils
                    .readInputStream(
                        { dto.stream },
                        DefaultDataBufferFactory(),
                        65536
                    )
            )
        )
        .retrieve()
        .bodyToMono(fileIdClazz)
        .doFinally { dto.stream.close() }
        .subscribeOn(Schedulers.boundedElastic())

    override fun getFile(id: ID): Mono<InputStream> {
        return webClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .queryParam("id", id)
                    .build()
            }
            .accept(MediaType.APPLICATION_PDF)
            .buildByteArrayInputStream()
    }
}

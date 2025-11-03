package ru.alex3koval.docGenerator.appImpl.service

import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.alex3koval.docGenerator.appImpl.core.extensions.buildByteArrayInputStream
import ru.alex3koval.docGenerator.domain.service.UnoconvFacade
import java.io.InputStream

class UnoconvFacadeImpl(
    private val webClient: WebClient
) : UnoconvFacade {
    override fun convertToPdf(docx: InputStream): Mono<InputStream> = Mono
        .fromCallable { docx.readAllBytes() }
        .flatMap { fileBytes ->
            val multipartBuilder = MultipartBodyBuilder()
                .apply { part("file", fileBytes) }

            webClient
                .post()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("pdf")
                        .build()
                }
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_PDF)
                .body(
                    BodyInserters.fromMultipartData(
                        multipartBuilder.build()
                    )
                )
                .buildByteArrayInputStream()
        }
        .subscribeOn(Schedulers.boundedElastic())
}

package ru.alex3koval.docGenerator.appImpl.service

import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import reactor.util.retry.RetryBackoffSpec
import ru.alex3koval.docGenerator.appImpl.model.RetryConfigurations
import java.time.Duration
import java.util.function.Consumer

class RetryService(
    private val configProps: RetryConfigurations.Props
) {
    fun <T> withRetry(
        mono: Mono<T>,
        onAllRetriesFailed: Consumer<Throwable>
    ): Mono<T> = mono.retryWhen(
        withOnRetryExhaustedThrow(buildBaseRetrySpec(), onAllRetriesFailed)
    )

    private fun withOnRetryExhaustedThrow(
        baseRetry: RetryBackoffSpec,
        onAllRetriesFailed: Consumer<Throwable>
    ): RetryBackoffSpec = baseRetry
        .onRetryExhaustedThrow { spec: RetryBackoffSpec?, signal: Retry.RetrySignal? ->
            onAllRetriesFailed.accept(signal!!.failure())
            signal.failure()
        }

    private fun buildBaseRetrySpec(): RetryBackoffSpec = Retry
        .backoff(configProps.maxAttempts.toLong(), Duration.ofMillis(configProps.minDelay.toLong()))
        .jitter(configProps.jitter)
}

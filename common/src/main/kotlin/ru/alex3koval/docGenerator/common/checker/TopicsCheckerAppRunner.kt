package ru.alex3koval.docGenerator.common.checker

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import ru.alex3koval.docGenerator.domain.common.vo.Topic
import ru.alex3koval.eventingImpl.factory.KafkaTopicsFetcherFactory

class TopicsCheckerAppRunner(
    private val factory: KafkaTopicsFetcherFactory
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        factory
            .create()
            .thenAccept { topicsFromBroker ->
                val domainRawTopics: Set<String> = Topic
                    .entries
                    .map { topic -> topic.value }
                    .toSet()

                val changed: Boolean = topicsFromBroker.retainAll(domainRawTopics)

                if (!changed) {
                    return@thenAccept
                }

                throw RuntimeException(
                    "Топики в модуле домена и в брокере не совпадают:\n" +
                            "Топики в модуле домена: $domainRawTopics\n" +
                            "Топики в брокере: $topicsFromBroker"
                )
            }
    }
}

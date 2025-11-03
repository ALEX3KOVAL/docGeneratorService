package ru.alex3koval.docGenerator.eventHandlingApp.settings

import ru.alex3koval.docGenerator.domain.common.event.DocumentCreationHasBeenRequestedEvent
import ru.alex3koval.docGenerator.domain.common.vo.Topic
import ru.alex3koval.docGenerator.eventHandlingApp.listener.DocumentCreationHasBeenRequestedListener
import ru.alex3koval.kafkaEventer.ListenersRegistrationConfig

class ListenersRegistrationProps {
    val documentListenersProps: MutableList<ListenersRegistrationConfig.Props<*>> = mutableListOf()
    val allProps: MutableList<ListenersRegistrationConfig.Props<*>> = mutableListOf()

    init {
        val topics: List<String> = listOf(
            Topic.DOCUMENT_GENERATION_DLT.value
        )

        documentListenersProps.addAll(
            listOf(
                ListenersRegistrationConfig.Props(
                    topics,
                    DocumentCreationHasBeenRequestedListener::class.java,
                    DocumentCreationHasBeenRequestedEvent::class.java
                )
            )
        )
    }

    init {
        allProps.addAll(documentListenersProps)
    }
}

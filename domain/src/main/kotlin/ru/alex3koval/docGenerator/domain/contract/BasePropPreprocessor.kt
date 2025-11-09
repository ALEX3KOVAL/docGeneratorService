package ru.alex3koval.docGenerator.domain.contract

abstract class BasePropPreprocessor {
    abstract var clientPersonPhone: String
    abstract var clientPersonEmail: String
    abstract var clientPersonPatronymic: String

    open fun preprocess() {
        clientPersonPhone = clientPersonPhone
            .replace("\\D".toRegex(), "")
            .applyPhoneMask()

        if (clientPersonPhone.isNotBlank() && clientPersonEmail.isNotBlank()) {
            clientPersonEmail = "эл. почта: $clientPersonEmail,"
        }

        if (clientPersonPatronymic.isNotBlank()) {
            clientPersonPatronymic = " $clientPersonPatronymic"
        }
    }

    private fun String.applyPhoneMask(): String = "тел.: +${get(0)}-(${slice(1..3)})-" +
            "(${slice(4..6)})-${slice(7..8)}-${slice(9..10)}"
}

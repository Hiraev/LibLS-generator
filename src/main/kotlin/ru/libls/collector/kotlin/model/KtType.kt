package ru.libls.collector.kotlin.model

sealed class KtType {

    data class Generic(
        val name: String,
        val parameters: List<KtType>
    ) : KtType()

    data class Plain(
        val name: String
    ) : KtType()

    object Null: KtType()

    object Lambda : KtType()

    object Undefined : KtType()

}

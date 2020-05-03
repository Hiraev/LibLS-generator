package ru.libls.collector.kotlin.model

data class KtArg(
    val name: String,
    val type: KtType,
    val default: String?
)

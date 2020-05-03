package ru.libls.collector.kotlin.model

data class KtVal(
    val name: String,
    val type: KtType
) : KtNode

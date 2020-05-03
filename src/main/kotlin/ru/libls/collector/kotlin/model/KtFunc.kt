package ru.libls.collector.kotlin.model

data class KtFunc(
    val name: String,
    val self: KtType?,
    val returnType: KtType,
    val args: List<KtArg>
) : KtNode

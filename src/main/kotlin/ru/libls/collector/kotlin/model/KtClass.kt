package ru.libls.collector.kotlin.model

data class KtClass(
    val name: String,
    val nodes: List<KtNode>
) : KtNode

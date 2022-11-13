package ru.hiraev.libsl.declaration.model

data class Function(
        val name: String,
        val receiver: String?,
        val arguments: List<Argument>,
        val returnType: ArgumentType
)
